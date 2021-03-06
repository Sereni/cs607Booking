package view.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controller.Availability;
import controller.BankApi;
import controller.Calculator;
import controller.DatabaseHandler;
import model.BookingEventModel;
import model.ExtraService;
import model.Room;
import model.RoomType;

/**
 * A kind of hotel event which related to booking process in a hotel
 * @author Aida
 *
 */
public class BookingEventProcess extends HotelEventProcess {
	private ArrayList<Room> availableRooms;
	private ArrayList<ExtraService> allServices; 
	
	public BookingEventProcess() {
		model = new BookingEventModel();
	}
	
	/**
	 * It's main flow of booking and it's based on requirements
	 * 1. ask check in and check out dates from user
	 * 2. ask room type from user
	 * 3. find all available roomType rooms between checkIn and checkOut 
	 * 4. show available rooms
	 * 5. ask to choose one or more rooms from user
	 * 6. block room
	 * 7. ask for extra services
	 * 8. ask user email
	 * 9. calculate payment
	 * 10. show summary and ask confirmation from user
	 * 11. payment
	 * 12. insert this booking in database
	 */
	@Override
	protected void doEvent() {
		askBookingDates();
		RoomType roomType = askRoomType();
		availableRooms = Availability.findAvailableRooms(model.checkIn, model.checkOut, roomType);
		showAvailableRooms();
		askForRooms();
		for ( Room room : model.rooms ) {
			blockRoom( room );
		}
		
		//after blocking rooms until finishing payment it should not take more than 15 mins!
		askForExtraServices();
		model.userEmail = askUserEmail();
		model.payment = new Calculator().getPayment((BookingEventModel) model);
		
		if ( userConfirmation() ) {
			BankApi.pay(model.payment);
		}
		/////until here

		new DatabaseHandler().makeBooking((BookingEventModel) model);	
	}
	
	private void askBookingDates() {
		System.out.println("Enter check-in date: (in this format dd/MM/yyyy)");
		model.checkIn = readDate();
		System.out.println("Enter check-out date: (in this format dd/MM/yyyy)");
		model.checkOut = readDate();
		
		// check-in should be after today AND
		// check-out should be at least one day after check-out
		Date today = new Date();
		if ( (model.checkIn.getTime() - today.getTime())/(1000*60*60*24) < 1 ||
				(model.checkOut.getTime() - model.checkIn.getTime())/(1000*60*60*24) < 1 ) {
			System.out.println("Dates you entered is not correct, please try again");
			askBookingDates();
		}
	}
	
	private Date readDate() {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			date = (Date)formatter.parse(in.readLine());
		} catch (ParseException e) { //TODO: handle exception
			e.printStackTrace();
			date = new Date();
		} catch (IOException e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}
	
	private RoomType askRoomType() {
		RoomType selectedRoomType;
		
		//ask from user
		System.out.println("Which room type do you want to search for? (please select by entering the index)");
		
		//show all available room type to the user
		int index = 1;
		for (RoomType type : RoomType.values()) {
			System.out.println((index++)+". "+type);
		}
		
		//user enters the room type index
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//TODO:handle exception
		try {
			int selectedIndex = Integer.parseInt(in.readLine());
			selectedRoomType = RoomType.values()[selectedIndex-1]; //map index to room type
		} catch (Exception e) {
			selectedRoomType = RoomType.Single;
			e.printStackTrace();
		}
		return selectedRoomType;
	}
	
	private void showAvailableRooms() {
		System.out.println("You can choose between following rooms:");
		for ( Room room:availableRooms ) {
			System.out.println(room);
		}
	}

	private void askForExtraServices() {
		System.out.println("You can add these services to your booking too\n"
				+ "Please enter number of each services do you want with x number of date/time. (e.g. 1x2,2x5 means service1 for 2 times and service2 for 5 times)");
		
		allServices = new DatabaseHandler().getActiveServices();
		for ( ExtraService service : allServices ) {
			System.out.println(service);
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String selectedServices = "";
		//TODO:handle exception
		try {
			selectedServices = in.readLine(); //read string of ids and amounts
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] idsAmountStr = selectedServices.split(","); //split id of each room
		// cast id from string to int and find the room
		for(int i = 0; i < idsAmountStr.length; i++) {
			String[] detail = idsAmountStr[i].split("x");
			//TODO:handle cast exception
			model.services.put(findServiceBetweenServices(Integer.parseInt(detail[0])), Integer.parseInt(detail[1]));
		}
		
	}
	
	// e.g. 123,125 equals user wants rooms with room id 123 and 125
	private void askForRooms() {
		System.out.println("Please enter id of rooms that you like to book: (you can enter multiple room by ,)");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String selectedRoomsStr = "";
		//TODO:handle exception
		try {
			selectedRoomsStr = in.readLine(); //read string of ids
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] idsStr = selectedRoomsStr.split(","); //split id of each room
		// cast id from string to int and find the room
		for(int i = 0; i < idsStr.length; i++) {
			model.rooms.add(findRoomBetweenAvailable(Integer.parseInt(idsStr[i])));
		}
		
	}
	
	private Room findRoomBetweenAvailable(int id) {
		for ( Room room :availableRooms )
			if ( room.getId() == id )
				return room;
		return null; //TODO: better way
	}

	
	private ExtraService findServiceBetweenServices(int id) {
		for ( ExtraService service : allServices )
			if ( service.getId() == id )
				return service;
		return null; //TODO: better way
	}
	
	private void blockRoom(Room room){
		for(Date d = model.checkIn; d.compareTo(model.checkOut) < 0; d = new Date(d.getTime() + (1000*60*60*24))) {
			room.addBooked(d);
		}
	}
	
	protected boolean userConfirmation() {
		System.out.println("\nYou want to book:");
		System.out.println(model);
		System.out.println("Do you confirm? (y/n)");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			if ( in.readLine().equals("y") )
				return true;
		} catch (IOException e) {
			// TODO Handle
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void updateBlockedRooms() {
		// TODO Auto-generated method stub
		
	}
}

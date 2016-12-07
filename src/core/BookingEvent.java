package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import persistence.DatabaseHandler;

/**
 * A kind of hotel event which related to booking process in a hotel
 * @author Aida
 *
 */
public class BookingEvent extends HotelEvent{
	private ArrayList<Room> availableRooms;
	
	public BookingEvent() {
	}
	
	public BookingEvent(int id, Date checkIn, Date checkOut, String userEmail, ArrayList<Room> rooms, int payment) {
		super(id, checkIn, checkOut, userEmail, rooms, payment);
	}

	/**
	 * It's main flow of booking and it's based on requirements
	 * 1. ask check in and check out dates from user
	 * 2. ask room type from user
	 * 3. find all available roomType rooms between checkIn and checkOut 
	 * 4. show available rooms
	 * 5. ask to choose one or more rooms from user
	 * 6. block room
	 * 7. ask user email
	 * 8. calculate payment
	 * 9. show summary and ask confirmation from user
	 * 10. payment
	 * 11. insert this booking in database
	 */
	@Override
	protected void doEvent() {
		askBookingDates();
		RoomType roomType = askRoomType();
		availableRooms = Availability.findAvailableRooms(checkIn, checkOut, roomType);
		showAvailableRooms();
		askForRooms();
		for ( Room room : rooms ) {
			blockRoom( room );
		}
		askUserEmail();
		payment = Calculator.getInstance().getPayment(this);
		
		if ( userConfirmation() ) {
			BankApi.pay(payment);
		}
		try {
			new DatabaseHandler().makeBooking(this);
		} catch (SQLException e) {
			// TODO  handle
			e.printStackTrace();
		}
	}
	
	private void askBookingDates() {
		System.out.println("Enter check-in date: (in this format dd/MM/yyyy)");
		checkIn = readDate();
		System.out.println("Enter check-out date: (in this format dd/MM/yyyy)");
		checkOut = readDate();
		
		// check-in should be after today AND
		// check-out should be at least one day after check-out
		Date today = new Date();
		if ( (checkIn.getTime() - today.getTime())/(1000*60*60*24) < 1 ||
				(checkOut.getTime() - checkIn.getTime())/(1000*60*60*24) < 1 ) {
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
			selectedRoomType = RoomType.values()[selectedIndex]; //map index to room type
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
			rooms.add(findRoomBetweenAvailable(Integer.parseInt(idsStr[i])));
		}
		
	}
	
	private Room findRoomBetweenAvailable(int id) {
		for ( Room room :availableRooms )
			if ( room.getId() == id )
				return room;
		return null; //TODO: better way
	}
	
	private void blockRoom(Room room){
		for(Date d = checkIn; d.compareTo(checkOut) < 0; d = new Date(d.getTime() + (1000*60*60*24))) {
			room.addBooked(d);
		}
	}
	
	protected boolean userConfirmation() {
		System.out.println("You wan to book these rooms:");
		for ( Room room:rooms ) {
			System.out.println(room);
		}
		System.out.println("Payment: "+payment+"$");
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
}

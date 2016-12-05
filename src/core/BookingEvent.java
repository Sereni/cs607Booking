package core;

import java.util.ArrayList;
import java.util.Date;

/**
 * A kind of hotel event which related to booking process in a hotel
 * @author Aida
 *
 */
public class BookingEvent extends HotelEvent{
	
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
		//TODO: ask room type from user and pass to the below function
		ArrayList<Room> availableRooms = Availability.findAvailableRooms(checkIn, checkOut, RoomType.Single);
		
		showAvailableRooms();
		askForRooms();
		for ( Room room : rooms ) {
			blockRoom( room );
		}
		askUserEmail();
		
		double payment = Calculator.getInstance().getPayment(this);
		
		//TODO:get confirmation from user
		//TODO:payment
		//TODO:add this booking event to database
		
	}
	
	//TODO: ask dates from user and handle casting exception
	//TODO: check criteria about checkin and check out dates
	private void askBookingDates() {
		checkIn = new Date(2016, 10, 20);
		checkOut = new Date(2016, 12, 20);
	}
	
	//TODO: show available rooms array list to user
	private void showAvailableRooms() {
	}
	
	//TODO: ask from user based on showing available rooms
	// e.g. 123,125 equals user wants rooms with room id 123 and 125
	private void askForRooms() {
		rooms = new ArrayList<>();
		
	}
	
	void blockRoom(Room room){
		for(Date d = checkIn; d.compareTo(checkOut) < 0; d = new Date(d.getTime() + (1000*60*60*24))) {
			room.addBooked(d);
		}
	}
}

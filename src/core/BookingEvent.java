package core;

import java.util.ArrayList;
import java.util.Date;

public class BookingEvent extends HotelEvent{

	private ArrayList<Room> availableRooms;
	
	public BookingEvent(){
	}
	
	@Override
	protected void doEvent() {
		askBookingDates();
		//TODO: ask room type from user and pass to the below function
		availableRooms = Availability.findAvailableRooms(checkIn, checkOut, RoomType.Single);
		
		showAvailableRooms();
		askForRooms();
		for ( Room room : rooms ) {
			blockRoom( room );
		}
		askUserEmail();
		
		//TODO:read all booking rules from db
		pricingRules = new ArrayList<>();
		
		double payment = calculatePayment();
		
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

	@Override
	protected double calculatePayment() {
		double payment = 0;
		double multiplier;
		for (Date date = checkIn; date.compareTo(checkOut)<0; date = new Date(date.getTime() + (1000 * 60 * 60 * 24))){
			for ( HotelPricingRule rule : pricingRules ) {
				multiplier = rule.getMultiplier(date);
				for ( Room room : rooms ) {
					payment += room.getBasePrice()*multiplier;
				}
				if ( multiplier != 1 ) //we want to apply only one rule
					break;
			}
		}
		return payment;
	}

}

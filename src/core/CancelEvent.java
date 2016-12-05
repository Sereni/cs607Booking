package core;

import java.util.ArrayList;
import java.util.Date;

/**
 * A kind of hotel event which related to cancel process in a hotel
 * @author Aida
 *
 */
public class CancelEvent extends HotelEvent{

//	public CancelEvent(int id) {
//		this.id = id;
//	}

	public CancelEvent(int id, Date checkIn, Date checkOut, String userEmail, ArrayList<Room> rooms, double payment) {
		super(id, checkIn, checkOut, userEmail, rooms, payment);
	}

	/**
	 * It's main flow of booking and it's based on requirements
	 * 1. retrieve event related to this id from database
	 * 2. ask user email and check it
	 * 3. calculate refund
	 * 4. show summary and ask confirmation from user
	 * 5. pay back refund
	 * 6. make this canceled database
	 */
	protected void doEvent() {
		retrieveEvent();
		
		//TODO: make it quitable :D
		while ( !checkUserInfo(askUserEmail()) ) {
		}
		
		double refund = Calculator.getInstance().getPayment(this);
		//TODO: ask for confirmation
		//TODO: payment
		for ( Room room : rooms ) {
			cancelRoom(room);
		}
		//TODO: change this record in database
		
	}

	private void retrieveEvent() {
		//TODO: read the event with the id from database
		// this = DatabaseHandler.getBooking(id);
	}
	
	private boolean checkUserInfo(String email) {
		return email.equals(userEmail);
	}
	
	void cancelRoom(Room room) {
		for(Date d = checkIn; d.compareTo(checkOut)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
			room.removeBooked(d);
		}
	}

}

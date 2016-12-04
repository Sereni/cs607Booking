package core;

import java.util.Date;

public class CancelEvent extends HotelEvent{

	public CancelEvent(int id) {
		this.id = id;
	}
	
	protected void doEvent() {
		retrieveEvent();
		
		//TODO: make it quitable :D
		while ( !checkUserInfo(askUserEmail()) ) {
		}
		double payment = calculatePayment();
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

	@Override
	protected double calculatePayment() {
		// TODO Auto-generated method stub
		return 0;
	}

}

package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import persistence.DatabaseHandler;

/**
 * A kind of hotel event which related to cancel process in a hotel
 * @author Aida
 *
 */
public class CancelEvent extends HotelEvent{

	private int refund;
	
	public CancelEvent(int id) {
		this.id = id;
	}

	public CancelEvent(int id, Date checkIn, Date checkOut, String userEmail,
			ArrayList<Room> rooms, HashMap<ExtraService, Integer> services, int payment, int refund) {
		super(id, checkIn, checkOut, userEmail, rooms, services, payment);
		this.refund = refund;
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
		
		//TODO: make it quitable :D
		while ( !checkUserInfo(askUserEmail()) ) {
			System.out.println("You entered wrong email, please try again");
		}
	
		refund = Calculator.getInstance().getRefund(this);
		
		if ( userConfirmation() ) {
			BankApi.refund(refund);
			for ( Room room : rooms ) {
				cancelRoom(room);
			}
			try {
				new DatabaseHandler().cancelBooking(this);
			} catch (SQLException e) {
				// TODO Handle
				e.printStackTrace();
			}
		}
	}
	
	private boolean checkUserInfo(String email) {
		return email.equals(userEmail);
	}
	
	void cancelRoom(Room room) {
		for(Date d = checkIn; d.compareTo(checkOut)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
			room.removeBooked(d);
		}
	}

	public int getRefund() {return this.refund;}

	
	protected boolean userConfirmation() {
		System.out.println("You want to cancel booking with id "+id+" and contains these room(s) and service(s):");
		for ( Room room:rooms ) {
			System.out.println(room);
		}
		for (HashMap.Entry<ExtraService, Integer> entry : services.entrySet()){
		    System.out.println(entry.getKey() + " for " + entry.getValue() +" times.");
		}
		System.out.println("Payment: (what you paid before) "+payment+"$");
		System.out.println("Refund: (what we will pay you back if u cancel)"+refund+"$");
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

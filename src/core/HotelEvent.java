package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * abstract model for events in a hotel
 * each event contains id, checkIn, checkOut, user email and list of rooms
 * Booking and Cancel event (2 main event in our requirements) extends this class
 * @author Aida
 *
 */
public abstract class HotelEvent {

	protected int id;
	protected Date checkIn;
	protected Date checkOut;
	protected String userEmail;
	protected ArrayList<Room> rooms;
	protected double payment;
	
	public HotelEvent(int id, Date checkIn, Date checkOut, String userEmail, ArrayList<Room> rooms, double payment) {
		this.id = id;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.userEmail = userEmail;
		this.rooms = rooms;
		this.payment = payment;
	}

	/**
	 * start event
	 */
	public void start() {
		doEvent();
	}

	/**
	 * ask email from user
	 * @return email
	 */
	protected String askUserEmail() {
		String email = "";

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter your email:");
		try {
			email = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return email;
	}

	/**
	 * abstract function which should be implemented in children classes
	 */
	protected abstract void doEvent();

}

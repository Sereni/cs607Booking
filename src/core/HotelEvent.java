package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/**
 * abstract model for events in a hotel
 * each event contains id, checkIn, checkOut, user email and list of rooms
 * Booking and Cancel event (2 main event in our requirements) extends this class
 * @author Aida
 *
 */
public abstract class HotelEvent implements Runnable {

	public int id;
	public Date checkIn;
	public Date checkOut;
	public String userEmail;
	public ArrayList<Room> rooms;
	public int payment;
	public HashMap<ExtraService, Integer> services = new HashMap<>(); //services with amount of that
	
	public HotelEvent() {
		rooms = new ArrayList<>();
	}
	
	public HotelEvent(int id, Date checkIn, Date checkOut, String userEmail,
			ArrayList<Room> rooms, HashMap<ExtraService, Integer> services, int payment) {
		this();
		this.id = id;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.userEmail = userEmail;
		this.rooms = rooms;
		this.services = services;
		this.payment = payment;
	}

	/**
	 * run event
	 */
	public void run() {
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

	protected abstract boolean userConfirmation();
	
	/**
	 * abstract function which should be implemented in children classes
	 */
	protected abstract void doEvent();

}

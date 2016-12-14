package view.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.BlockedRooms;
import model.HotelEventModel;
/**
 * abstract model for events in a hotel
 * each event contains id, checkIn, checkOut, user email and list of rooms
 * Booking and Cancel event (2 main event in our requirements) extends this class
 * @author Aida
 *
 */

//template method
public abstract class HotelEventProcess implements Runnable {

	protected HotelEventModel model;
	
	protected BlockedRooms blockedRooms;
	
	/**
	 * run event
	 */
	public void run() {
		doEvent();
		System.out.println("Thank You!");
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
	
	protected abstract void update();

}

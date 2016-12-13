package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import model.ExtraService;
import model.HotelEventModel;
import model.Room;
/**
 * abstract model for events in a hotel
 * each event contains id, checkIn, checkOut, user email and list of rooms
 * Booking and Cancel event (2 main event in our requirements) extends this class
 * @author Aida
 *
 */
public abstract class HotelEvent implements Runnable {

	protected HotelEventModel model;
	
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
	
	protected void showListOfRooms() {
		int nights = (int) (( model.checkOut.getTime() - model.checkIn.getTime() ) / ( 1000*60*60*24)) ;
		for ( Room room : model.rooms ) {
			System.out.println(room+" for "+nights+" night(s).");
		}
	}
	
	protected void showListOfServices() {
		for (HashMap.Entry<ExtraService, Integer> entry : model.services.entrySet()){
		    System.out.println(entry.getKey() + " for " + entry.getValue() +" times.");
		}
	}

	protected abstract boolean userConfirmation();
	
	/**
	 * abstract function which should be implemented in children classes
	 */
	protected abstract void doEvent();

}

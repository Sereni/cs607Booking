package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import persistence.DatabaseHandler;

public class Runner {
	
	enum HotelEventType {
		BOOKING, CANCELING
	}
	
	public static void main(String[] args) {	
		try {
			factoryMethod(HotelEventType.BOOKING);	
			factoryMethod(HotelEventType.CANCELING);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void factoryMethod(HotelEventType type) throws InterruptedException {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			 new Thread ( new BookingEvent() ).start();
		}
		if ( type == HotelEventType.CANCELING ) {
			int id = 0;
			System.out.println("enter id of booking");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			try {
				id = Integer.parseInt(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) { //TODO: handle exception better
				e.printStackTrace();
			}
			//retrieve event
			try {
				CancelEvent cancelEvent = new DatabaseHandler().getCancelingBooking(id);
				 new Thread (cancelEvent).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

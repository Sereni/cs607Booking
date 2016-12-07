package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Runner {
	
	public static ArrayList<Room> blockedRoom;
	
	enum HotelEventType {
		BOOKING, CANCELING
	}
	
	public static void main(String[] args) {
		factoryMethod(HotelEventType.BOOKING);		
		factoryMethod(HotelEventType.CANCELING);
	}
	
	private static void factoryMethod(HotelEventType type) {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			new BookingEvent().start();
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
			new CancelEvent(id);
		}
		System.out.println("---------------------------");
	}
}

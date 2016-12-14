package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BookingEventModel extends HotelEventModel {

	public BookingEventModel() {
	}
	
	public BookingEventModel(int id, Date checkIn, Date checkOut, String userEmail,
			ArrayList<Room> rooms, HashMap<ExtraService, Integer> services, int payment) {
		super(id, checkIn, checkOut, userEmail, rooms, services, payment);
	}

	@Override
	public String toString() {

		String str = "Booking:\n";
		int nights = (int) (( checkOut.getTime() - checkIn.getTime() ) / ( 1000*60*60*24)) ;
		for ( Room room : rooms ) {
			str += (room+" for "+nights+" night(s).\n");
		}
		for (HashMap.Entry<ExtraService, Integer> entry : services.entrySet()){
		    str += (entry.getKey() + " for " + entry.getValue() +" times.\n");
		}
		str += ("Payment: "+payment+"$\n");
		return str;
	}

}

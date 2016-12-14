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
		str += super.toString();
		str += ("Payment: "+payment+"$\n");
		return str;
	}

}

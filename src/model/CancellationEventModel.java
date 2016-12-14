package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CancellationEventModel extends HotelEventModel {

	public int refund;
	
	public CancellationEventModel(int id) {
		this.id = id;
	}

	public CancellationEventModel(int id, Date checkIn, Date checkOut, String userEmail,
			ArrayList<Room> rooms, HashMap<ExtraService, Integer> services, int payment, int refund) {
		super(id, checkIn, checkOut, userEmail, rooms, services, payment);
		this.refund = refund;
	}

	@Override
	public String toString() {
		String str = "Cancellation:\n";
		str += super.toString();
		str += ("Payment: (what you paid before) "+payment+"$\n");
		str += ("Refund: (what we will pay you back if u cancel) "+refund+"$");
		return str;
	}
}

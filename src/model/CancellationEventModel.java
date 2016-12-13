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
		// TODO Auto-generated method stub
		return null;
	}
}

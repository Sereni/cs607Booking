package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class HotelEventModel {
	
	public int id;
	public Date checkIn;
	public Date checkOut;
	public String userEmail;
	public ArrayList<Room> rooms;
	public int payment;
	public HashMap<ExtraService, Integer> services = new HashMap<>(); //services with amount of that
	
	public HotelEventModel() {
		rooms = new ArrayList<>();
	}
	
	public HotelEventModel(int id, Date checkIn, Date checkOut, String userEmail,
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
	
	public abstract String toString();

}

package model;

import java.text.SimpleDateFormat;
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
	
	public String toString() {
		String str = "";
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		str += dateFormatter.format(checkIn) +" to "+dateFormatter.format(checkOut)+"\n";
		int nights = (int) (( checkOut.getTime() - checkIn.getTime() ) / ( 1000*60*60*24)) ;
		for ( Room room : rooms ) {
			str += (room+" for "+nights+" night(s).\n");
		}
		for (HashMap.Entry<ExtraService, Integer> entry : services.entrySet()){
		    str += (entry.getKey() + " for " + entry.getValue() +" times.\n");
		}
		return str;
	}

}

package core;

import java.util.ArrayList;
import java.util.Date;

public class Customer {
	
	private String name;
	private String email_id;
	private Date check_in;
	private Date check_out;
	public ArrayList<Room> room = new ArrayList<Room>();
	
	public ArrayList<Room> getRooms() {
		return room;
	}
	public void addRoom(Room room) {
		
		for(Date d = check_in; d.compareTo(check_out)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
			room.setBooked(d);
		}
		this.room.add(room);
		
	}
	public Date getCheck_in() {
		return check_in;
	}
	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}
	public Date getCheck_out() {
		return check_out;
	}
	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

}

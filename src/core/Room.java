package core;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

enum RoomType {
	Single, Double, Studio, Suite
	};

public class Room {

	private int id;
	private RoomType type;
	private double basePrice;
	private Set<Date> booked = new HashSet<Date>();
	
	public Room( int id, RoomType type, double basePrice, Set<Date> booked ) {
		this.id = id;
		this.type = type;
		this.basePrice = basePrice;
		this.booked = booked;
	}

	public Room( int id, String type, double basePrice, Set<Date> booked ) {
		this.id = id;
		this.type = RoomType.valueOf(type);
		this.basePrice = basePrice;
		this.booked = booked;
	}
	
	public Set<Date> getBooked() {
		return booked;
	}
	
	public void addBooked(Date booked) {
		this.booked.add(booked);
	}
	
	public void removeBooked(Date dateToRemove){
		if(booked.contains(dateToRemove))
			booked.remove(dateToRemove);
	}
	
	public RoomType getType() {
		return type;
	}
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public String toString() {
		 return ("Room id: "+id+", base price: "+basePrice);
	}
	public int getId() {return id;}
}
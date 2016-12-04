package core;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


enum RoomType {
	Single, Double, Studio, Suite
	};

//TODO: add toString function
public class Room {

	private int id;
	private RoomType type;
	private double basePrice = 100; // base price
	private Set<Date> booked = new HashSet<Date>();
	
	public Room( int id, RoomType type ) {
		this.id = id;
		this.type = type;
		
		switch (type) {
		case Double:
			this.basePrice = 2*basePrice - 5;
			break;
		case Studio:
			this.basePrice = 2*basePrice + 50;
			break;
		case Suite:
			this.basePrice = 2*basePrice + 100;
			break;
		default:
			break;			
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId( int id ) {
		this.id = id;
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
	////////ImMutable///////
	private void setType(RoomType type) {
		this.type = type;
	}
	
	public double getBasePrice() {
		return basePrice;
	}
	
	private void setBasePrice(int price) {
		this.basePrice = price;
	}
	
}
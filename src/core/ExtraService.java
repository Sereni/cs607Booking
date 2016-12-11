package core;

public class ExtraService {
	private int id;
	private String name;
	private int price;
	
	public ExtraService(int id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}

	public int getId() {
		return id;
	}
	
	public String toString() {
		return "Service id: "+id+", "+name + " -> "+price+"$"; 
	}
}

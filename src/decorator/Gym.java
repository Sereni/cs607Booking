package decorator;

public class Gym extends Decorator{

	public Gym(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : Gym";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 10;
		
	}
}

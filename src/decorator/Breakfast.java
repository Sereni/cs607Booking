package decorator;

public class Breakfast extends Decorator{

	public Breakfast(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : Breakfast";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 30;
		
	}

}

package decorator;

public class Massage extends Decorator{

	public Massage(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : Massage";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 20;
		
	}

}

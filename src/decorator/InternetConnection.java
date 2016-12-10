package decorator;

public class InternetConnection extends Decorator{

	public InternetConnection(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : InternetConnection";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 20;
		
	}

}

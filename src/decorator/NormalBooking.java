package decorator;

public class NormalBooking extends Booking{

	@Override
	public String Description() {
		
		return "Booking";
		
	}

	@Override
	public int Price() {
		
		return 100;
		
	}

}

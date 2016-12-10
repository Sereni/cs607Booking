package decorator;

public abstract class Decorator extends Booking{

	public Decorator(Booking booking) {
		
		this.booking = booking;
	}

	Booking booking;
}

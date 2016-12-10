package decorator;

public class InRoomSafe extends Decorator{

	public InRoomSafe(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : InRoomSafe";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 10;
		
	}

}

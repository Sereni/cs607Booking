package decorator;

public class DailyNewspaperDelivery extends Decorator{

	public DailyNewspaperDelivery(Booking booking){
		super(booking);
	}
	
	@Override
	public String Description() {
		
		return  booking.Description() + " : DailyNewspaperDelivery";
		
	}

	@Override
	public int Price() {
		
		return booking.Price() + 5;
		
	}

}

package decorator;


public class Runner {

	public static void main(String[] args) {
		
		Booking booking = new InRoomSafe(new DailyNewspaperDelivery(new Gym(new InternetConnection(new NormalBooking()))));
		
		System.out.println(booking.Description());
		System.out.println(booking.Price());
		
	}
	
}

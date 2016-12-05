package core;

public class Runner {
	
	public static void main(String[] args) {
		new BookingEvent().start();
		new CancelEvent(12).start();
	}
}

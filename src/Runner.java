import view.BookingEventProcess;
import view.CancellationEvent;

public class Runner {
	
	enum HotelEventType {
		BOOKING, CANCELING
	}
	
	public static void main(String[] args) {	
		try {
//			factoryMethod(HotelEventType.BOOKING);	
			factoryMethod(HotelEventType.CANCELING);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void factoryMethod(HotelEventType type) throws InterruptedException {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			 new Thread ( new BookingEventProcess() ).start();
		}
		if ( type == HotelEventType.CANCELING ) {

			 new Thread ( new CancellationEvent() ).start();
		}
	}
}

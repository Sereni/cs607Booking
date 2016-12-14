
import view.commandline.BookingEventProcess;
import view.commandline.CancellationEventProcess;
import view.gui.EnterDetailsWindow;
import view.gui.FirstWindow;

public class Runner {
	
	enum HotelEventType {
		BOOKING, CANCELING
	}
	
	public static void main(String[] args) {	
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FirstWindow().setVisible(true);
            }
        });		
//		try {
//			factoryMethod(HotelEventType.BOOKING);	
////			factoryMethod(HotelEventType.CANCELING);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static void factoryMethod(HotelEventType type) throws InterruptedException {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			 new Thread ( new BookingEventProcess() ).start();
		}
		if ( type == HotelEventType.CANCELING ) {

			 new Thread ( new CancellationEventProcess() ).start();
		}
	}
}

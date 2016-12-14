
import view.commandline.BookingEventProcess;
import view.commandline.CancellationEventProcess;
import view.gui.FirstWindow;

public class Runner {

	private static final boolean runFromCL = false;

	enum HotelEventType {
		BOOKING, CANCELING
	}

	public static void main(String[] args) {	
		if (runFromCL) {
			try {
				factoryMethod(HotelEventType.BOOKING);	
				factoryMethod(HotelEventType.CANCELING);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new FirstWindow().setVisible(true);
				}
			});		
		}

	}

	private static void factoryMethod(HotelEventType type) throws InterruptedException {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			new Thread ( new BookingEventProcess() ).start();
		}
		if ( type == HotelEventType.CANCELING ) {

			try {
				new Thread ( new CancellationEventProcess() ).start();
			} catch (Exception e) {
				System.out.println("No booking with this id or it was cancelled before");
			}
		}
	}
}

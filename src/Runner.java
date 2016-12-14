
import java.util.ArrayList;

import model.Room;
import view.commandline.BookingEventProcess;
import view.commandline.CancellationEventProcess;
import view.commandline.HotelEventProcess;
import view.gui.FirstWindow;

public class Runner {

	private static final boolean runFromCL = false;
	private static HotelEventProcess event;
	
	//TODO: actually lock them
	ArrayList<Room> lockedRooms;
	
	enum HotelEventType {
		BOOKING, CANCELING
	}

	public static void main(String[] args) {	
		if (runFromCL) {
			try {
				simpleFactoryMethod(HotelEventType.BOOKING);	
				if ( event != null )
					new Thread ( event ).start();
//				simpleFactoryMethod(HotelEventType.CANCELING);
//				if ( event != null )
//					new Thread ( event ).start();
			} catch (InterruptedException e) {
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

	private static void simpleFactoryMethod(HotelEventType type) throws InterruptedException {
		System.out.println(type);
		if ( type == HotelEventType.BOOKING ) {
			event = new BookingEventProcess();
		}
		if ( type == HotelEventType.CANCELING ) {

			try {
				event = new CancellationEventProcess();
			} catch (Exception e) {
				System.out.println("No booking with this id or it was cancelled before");
			}
		}
	}
}

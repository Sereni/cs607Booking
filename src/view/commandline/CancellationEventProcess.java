package view.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;

import controller.BankApi;
import controller.Calculator;
import controller.DatabaseHandler;
import model.CancellationEventModel;
import model.Room;

/**
 * A kind of hotel event which related to cancel process in a hotel
 * @author Aida
 *
 */
public class CancellationEventProcess extends HotelEventProcess{

	public CancellationEventProcess() throws Exception {
		int id = 0;
		System.out.println("Enter id of booking");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			id = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) { //TODO: handle exception better
			e.printStackTrace();
		}
		//retrieve event
		
		model = new DatabaseHandler().getCancelingBooking(id);
		if (model == null) throw new Exception();
		
	}
	/**
	 * It's main flow of booking and it's based on requirements
	 * 1. retrieve event related to this id from database
	 * 2. ask user email and check it
	 * 3. calculate refund
	 * 4. show summary and ask confirmation from user
	 * 5. pay back refund
	 * 6. make this canceled database
	 */
	protected void doEvent() {
		
		//TODO: make it quitable :D
		while ( !checkUserInfo(askUserEmail()) ) {
			System.out.println("You entered wrong email, please try again");
		}
	
		((CancellationEventModel)model).refund = Calculator.getInstance().getRefund((CancellationEventModel) model);
		
		if ( userConfirmation() ) {
			BankApi.refund(((CancellationEventModel)model).refund);
			for ( Room room : model.rooms ) {
				cancelRoom(room);
			}
			try {
				new DatabaseHandler().cancelBooking((CancellationEventModel)model);
			} catch (SQLException e) {
				// TODO Handle
				e.printStackTrace();
			}
		}
	}
	
	private boolean checkUserInfo(String email) {
		return email.equals(model.userEmail);
	}
	
	void cancelRoom(Room room) {
		for(Date d = model.checkIn; d.compareTo(model.checkOut)<0; d = new Date(d.getTime() + (1000 * 60 * 60 * 24))){
			room.removeBooked(d);
		}
	}
	
	protected boolean userConfirmation() {
		System.out.println("You want to cancel booking with id "+model.id+":");
		System.out.println(model);
		System.out.println("Do you confirm? (y/n)");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			if ( in.readLine().equals("y") )
				return true;
		} catch (IOException e) {
			// TODO Handle
			e.printStackTrace();
		}
		return false;
	}
}

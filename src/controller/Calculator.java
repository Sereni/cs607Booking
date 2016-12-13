package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model.BookingEventModel;
import model.BookingPricingRule;
import model.CancellationPricingRules;
import model.CancellationEventModel;
import model.ExtraService;
import model.HotelEventModel;
import model.HotelPricingRule;
import model.Room;


/**
 * singelton calculator
 * @author aida
 *
 */
public class Calculator {

	private static Calculator instance;
	private static ArrayList<BookingPricingRule> bookingPricingRules;
	private static ArrayList<CancellationPricingRules> cancellationPricingRules;

	private Calculator() {
		initBookingPricingRules();
		initCancellationPricingRules();
	}
	private void initBookingPricingRules() {
		try {
			bookingPricingRules = new DatabaseHandler().getPricingRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initCancellationPricingRules() {
		try {
			cancellationPricingRules = new DatabaseHandler().getCancelingRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized Calculator getInstance() {
		if ( instance == null )
			return new Calculator();
		return instance;
	}

	/**
	 * first calculate price of rooms based on number of nights and pricing rules
	 * then add price of services
	 * @param booking
	 * @return
	 */
	public int getPayment(BookingEventModel booking) {
		int payment = 0;
		double multiplier=1;
		for (Date date = booking.checkIn; date.compareTo(booking.checkOut)<0; date = new Date(date.getTime() + (1000 * 60 * 60 * 24))) {
				for ( Room room : booking.rooms ) {
					for ( HotelPricingRule rule : bookingPricingRules ) {
						multiplier = rule.getMultiplier(date);
					}
					payment += room.getBasePrice()*multiplier;
					if ( multiplier != 1 ) //we want to apply only one rule
							break;
				}
		}
		payment += getPriceForServices(booking);
		return payment;
	}

	/**
	 * as we return back whole amount of price of services so first subtract that amount and multiply cancel rules to payment related to rooms
	 * after that refund both of them
	 * (all happens if check in is before today)
	 * @param cancel
	 * @return
	 */
	public int getRefund(CancellationEventModel cancel) {
		Date today = new Date();
		if ( today.compareTo(cancel.checkIn) < 0 ) {
			int servicesPrice = getPriceForServices(cancel);
			int roomsPrice = cancel.payment - servicesPrice;
			int refundOfRooms = roomsPrice;
			double multiplier;
			for ( HotelPricingRule rule : cancellationPricingRules ) {
				multiplier = rule.getMultiplier(cancel.checkIn);
				refundOfRooms = (int) (multiplier*refundOfRooms);
				if ( multiplier != 1 )
					break;
			}
			return refundOfRooms+servicesPrice;
		}
		return 0; //no refund for booking that passed
	}
	
	private int getPriceForServices(HotelEventModel event) {
		int payment = 0;
		for (HashMap.Entry<ExtraService, Integer> entry : event.services.entrySet()){
			payment += entry.getKey().getPrice() * entry.getValue();
		}
		return payment;
	}
}
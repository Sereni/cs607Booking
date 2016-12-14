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
 *  calculator
 * @author aida
 *
 */
public class Calculator {
	private static ArrayList<BookingPricingRule> bookingPricingRules;
	private static ArrayList<CancellationPricingRules> cancellationPricingRules;

	private static void initBookingPricingRules() {
		try {
			bookingPricingRules = new DatabaseHandler().getPricingRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void initCancellationPricingRules() {
		try {
			cancellationPricingRules = new DatabaseHandler().getCancelingRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * first calculate price of rooms based on number of nights and pricing rules
	 * then add price of services
	 * @param booking
	 * @return
	 */
	public static int getPayment(BookingEventModel booking) {

		initBookingPricingRules();
		int payment = 0;
		for (Date date = booking.checkIn; date.compareTo(booking.checkOut)<0; date = new Date(date.getTime() + (1000 * 60 * 60 * 24))) {

			double multiplier=1;
			for ( HotelPricingRule rule : bookingPricingRules ) {
				multiplier = rule.getMultiplier(date);
				if ( multiplier != 1 ) //we want to apply only one rule
					break;
			}
			for ( Room room : booking.rooms ) {
				payment += room.getBasePrice()*multiplier;
				System.out.println(room.getBasePrice()+" * "+String.format("%.2f", multiplier)+" + ");
			}
		}
		int servicePayment = getPriceForServices(booking);
		payment += servicePayment;
		System.out.println(servicePayment+"\n= "+payment);
		return payment;
	}

	/**
	 * as we return back whole amount of price of services so first subtract that amount and multiply cancel rules to payment related to rooms
	 * after that refund both of them
	 * (all happens if check in is before today)
	 * @param cancel
	 * @return
	 */
	public static int getRefund(CancellationEventModel cancel) {
		initCancellationPricingRules();
		Date today = new Date();
		if ( today.compareTo(cancel.checkIn) < 0 ) {
			int servicesPrice = getPriceForServices(cancel);
			int roomsPrice = cancel.payment - servicesPrice;
			int refundOfRooms = roomsPrice;
			double multiplier = 1;
			for ( HotelPricingRule rule : cancellationPricingRules ) {
				multiplier = rule.getMultiplier(cancel.checkIn);
				refundOfRooms = (int) (multiplier*refundOfRooms);
				if ( multiplier != 1 )
					break;
			}

			System.out.println(refundOfRooms+"(mul="+String.format("%.2f", multiplier)+")"+" + "+servicesPrice);
			return refundOfRooms+servicesPrice;
		}
		return 0; //no refund for booking that passed
	}

	private static int getPriceForServices(HotelEventModel event) {
		int payment = 0;
		for (HashMap.Entry<ExtraService, Integer> entry : event.services.entrySet()){
			payment += entry.getKey().getPrice() * entry.getValue();
		}
		return payment;
	}
}

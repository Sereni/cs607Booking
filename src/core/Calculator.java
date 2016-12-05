package core;

import java.util.ArrayList;
import java.util.Date;

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
		//TODO: read from db
		bookingPricingRules = new ArrayList<>();
	}
	private void initCancellationPricingRules() {
		//TODO: read from db
		cancellationPricingRules = new ArrayList<>();
	}

	public static synchronized Calculator getInstance() {
		if ( instance == null )
			return new Calculator();
		return instance;
	}

	public double getPayment(BookingEvent booking) {
		double payment = 0;
		double multiplier;
		for (Date date = booking.checkIn; date.compareTo(booking.checkOut)<0; date = new Date(date.getTime() + (1000 * 60 * 60 * 24))){
			for ( HotelPricingRule rule : bookingPricingRules ) {
				multiplier = rule.getMultiplier(date);
				for ( Room room : booking.rooms ) {
					payment += room.getBasePrice()*multiplier;
				}
				if ( multiplier != 1 ) //we want to apply only one rule
					break;
			}
		}
		return payment;
	}


	public double getPayment(CancelEvent cancel) {
		double payment = 0;
		double multiplier;
		for ( HotelPricingRule rule : cancellationPricingRules ) {
			multiplier = rule.getMultiplier(cancel.checkIn);
			for ( Room room : cancel.rooms ) {
				payment += room.getBasePrice()*multiplier;
			}
			if ( multiplier != 1 ) //we want to apply only one rule
				break;
		}
		return payment;
	}
}

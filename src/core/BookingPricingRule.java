package core;

import java.util.ArrayList;
import java.util.Date;

public class BookingPricingRule extends HotelPricingRule {
	private ArrayList<Date> dates;

	/**
	 * check if date is in dates array --> return multiplier
	 		 else return 1
	 */
	@Override
	public float getMultiplier(Date date) {
		if ( dates.contains(date) )
			return multiplier;
		return 1;
	}
}

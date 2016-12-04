package core;

import java.util.ArrayList;
import java.util.Date;

public class BookingPricingRule extends HotelPricingRule {
	private ArrayList<Date> dates;

	@Override
	public float getMultiplier(Date date) {
		//TODO: check if date is in dates array --> return multiplier
		// else return 1
		return multiplier;
	}
}

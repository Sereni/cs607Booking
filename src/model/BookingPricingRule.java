package model;

import java.util.ArrayList;
import java.util.Date;

public class BookingPricingRule extends HotelPricingRule {
	private ArrayList<Date> dates;

    public BookingPricingRule(float multiplier, ArrayList<Date> dates) {
        this.dates = dates;
        this.multiplier = multiplier;
    }

	/**
	 * check if date is in dates array --> return multiplier
	 		 else return 1
	 */
	@Override
	public float getMultiplier(Date date) {
		for ( Date ruleDate : dates ) {
			int subtract = (int) Math.abs( (date.getTime() - ruleDate.getTime())/(1000*60*60*24) );
			if ( subtract == 0 )
				return multiplier;
		}
		return 1;
	}
}

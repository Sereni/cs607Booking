package core;

import java.util.Date;

public class CancellationPricingRules extends HotelPricingRule {
	private int lowBoundary;
	private int topBoundary;
	
	/**
	 * check if lowBoundary<(date-today)<topBoundary --> return multiplier
			 else return 1
	 */
	@Override
	public float getMultiplier(Date date) {
		Date today = new Date();
		int subtraction = (int) ((date.getTime()-today.getTime())/1000*60*60*24);
		if ( subtraction >= lowBoundary && subtraction <= topBoundary )
			return multiplier;
		return 1;
	}

}

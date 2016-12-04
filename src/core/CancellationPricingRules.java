package core;

import java.util.Date;

public class CancellationPricingRules extends HotelPricingRule {
	private int lowBoundary;
	private int topBoundary;
	
	@Override
	public float getMultiplier(Date date) {
		//TODO: check if lowBoundary<(date-today)<topBoundary --> return multiplier
		// else return 1
		return multiplier;
	}

}

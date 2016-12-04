package core;

import java.util.Date;

abstract public class HotelPricingRule {
	protected float multiplier;
	public abstract float getMultiplier(Date date);
}

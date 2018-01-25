package PriceStrategies;

/**
 * Base price strategy calculates the price simply by multiplying the base price with the amount of items.
 */
public class BasePriceStrategy implements PriceStrategy {
	private long basePrice = 0;

	public BasePriceStrategy(final long basePrice) {
		this.basePrice = basePrice;
	}

	public long getPrice(final long itemCount) {
		return itemCount * basePrice;
	}
}

package PriceStrategies;

/**
 * Created by chris on 24.01.2018.
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

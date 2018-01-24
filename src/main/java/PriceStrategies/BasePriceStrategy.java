package PriceStrategies;

/**
 * Created by chris on 24.01.2018.
 */
public class BasePriceStrategy implements PriceStrategy {
	private long basePrice = 0;

	public BasePriceStrategy(long basePrice) {
		this.basePrice = basePrice;
	}

	public long getPrice(int itemCount) {
		return itemCount * basePrice;
	}
}

package PriceStrategies;

/**
 * Created by chris on 24.01.2018.
 */
public class XForYStrategy implements PriceStrategy {

	private long basePrice = 0;
	private long discountNumber = 1;
	private long discountPrice = 0;

	public XForYStrategy(final long basePrice, final long num, final long price) {
		this.basePrice = basePrice;
		this.discountNumber = num;
		this.discountPrice = price;
	}

	public long getPrice(final long itemCount) {
		long discount = itemCount / discountNumber;
		long remainder = itemCount % discountNumber;

		return discount * discountPrice + remainder * basePrice;
	}
}

package PriceStrategies;

/**
 * X for y strategy calculates the price for multi priced items e.g.
 * buy x of them, and they'll cost you y.
 * The base price is applied to any leftover items.
 */
public class XForYStrategy implements PriceStrategy {

	private long basePrice = 0;
	private long discountNumber = 1;
	private long discountPrice = 0;

	/**
	 * Constructor
	 * @param basePrice The base price of the item
	 * @param num       The number you have to buy to apply the discount (this is our 'x')
	 * @param price     The price you get when you buy 'num' items (this is our 'y')
	 */
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

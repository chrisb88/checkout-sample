import PriceStrategies.BasePriceStrategy;
import PriceStrategies.PriceStrategy;
import PriceStrategies.XForYStrategy;
import org.json.simple.JSONObject;

/**
 * PriceItem represents an item in the shop.
 * It can calculate it's price on different strategies.
 */
public class PriceItem {

	public class UnknownStrategyException extends RuntimeException {}

	private String name;
	private long basePrice;
	private PriceStrategy priceStrategy;

	/**
	 * Constructor
	 * @param name         Name of the item
	 * @param basePrice    Base price of the item
	 * @param strategy     Strategy to use for price calculation
	 */
	public PriceItem(final String name, final long basePrice, final JSONObject strategy) {
		this.name = name;
		this.basePrice = basePrice;

		applyStrategy(strategy);
	}

	public String getName() {
		return name;
	}

	public long getPrice(final long itemCount) {
		return priceStrategy.getPrice(itemCount);
	}

	/**
	 * Applies the given strategy.
	 * If none given, a default strategy is applied.
	 * @param strategy Strategy to apply
	 */
	private void applyStrategy(final JSONObject strategy) {
		if (strategy == null) {
			applyDefaultStrategy();
		} else {
			final String id = (String) strategy.get("id");
			switch (id) {
				case "xForY":
					final long num = (long) strategy.get("num");
					final long price = (long) strategy.get("price");
					priceStrategy = new XForYStrategy(basePrice, num, price);
					break;
				default:
					throw new UnknownStrategyException();
			}
		}
	}

	/**
	 * Applies the default strategy.
	 * In this case the BasePriceStrategy.
	 */
	private void applyDefaultStrategy() {
		priceStrategy = new BasePriceStrategy(basePrice);
	}
}

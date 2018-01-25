import PriceStrategies.BasePriceStrategy;
import PriceStrategies.PriceStrategy;
import PriceStrategies.XForYStrategy;
import org.json.simple.JSONObject;

/**
 * Created by chris on 24.01.2018.
 */
public class PriceItem {

	public class UnknownStrategyException extends RuntimeException {}

	private String name;
	private long basePrice;
	private PriceStrategy priceStrategy;

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

	private void applyDefaultStrategy() {
		priceStrategy = new BasePriceStrategy(basePrice);
	}
}

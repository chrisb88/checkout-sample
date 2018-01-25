import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

/**
 * Created by chris on 24.01.2018.
 */
public class Checkout {

	private static final String PRICE_CONFIG_FILE = "./conf/prices.json";

	public class InvalidItemException extends RuntimeException {
		public InvalidItemException(String message) {
			super(message);
		}
	}

	private Set<PriceItem> priceItems = new HashSet<>();
	private CountableList<String> basket = new CountableList<>();

	public Checkout(final JSONObject prices) {
		final JSONArray items = (JSONArray) prices.get("items");
		for (JSONObject item : (Iterable<JSONObject>) items) {
			final String name = (String) item.get("name");
			final long basePrice = (long) item.get("basePrice");
			final JSONObject strategy = (JSONObject) item.get("priceStrategy");
			priceItems.add(new PriceItem(name, basePrice, strategy));
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
		} else {
			final String goods = args[0];
			final Checkout co = new Checkout(readRules());
			System.out.println(String.format("%s = %s", goods, co.processItemString(goods)));
		}
	}

	private static void printUsage() {
		// todo
		System.out.println("Use me right!");
	}

	public void scan(final char item) {
		if (!itemIsValid(item)) {
			throw new InvalidItemException(String.format("Unknown item '%s'", item));
		}

		basket.add(String.valueOf(item));
	}

	public long total() {
		long amount = 0;

		final Map<String, Integer> counts = basket.getCounts();
		for (Map.Entry<String, Integer> item : counts.entrySet()) {
			final PriceItem priceItem = getPriceItem(item.getKey());
			final int itemCount = item.getValue();

			if (priceItem != null) {
				amount += priceItem.getPrice(itemCount);
			}
		}

		return amount;
	}

	public void clearBasket() {
		basket.clear();
	}

	private boolean itemIsValid(final char item) {
		return getPriceItem(String.valueOf(item)) != null;
	}

	private PriceItem getPriceItem(final String name) {
		for (PriceItem item : priceItems) {
			if (item.getName().equals(name)) {
				return item;
			}
		}

		return null;
	}

	public static JSONObject readRules() {
		final JSONParser parser = new JSONParser();

		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(new FileReader(PRICE_CONFIG_FILE));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return obj;
	}

	public long processItemString(final String itemString) {
		for (int i = 0; i < itemString.length(); i++) {
			scan(itemString.charAt(i));
		}

		return total();
	}
}

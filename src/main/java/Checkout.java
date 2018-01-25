import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

/**
 * This class is responsible for the whole checkout process and calculates a total price.
 */
public class Checkout {

	/**
	 * Item and price configuration file
	 */
	private static final String PRICE_CONFIG_FILE = "./conf/prices.json";

	public class InvalidItemException extends RuntimeException {
		public InvalidItemException(String message) {
			super(message);
		}
	}

	private Set<PriceItem> priceItems = new HashSet<>();
	private CountableList<String> basket = new CountableList<>();

	/**
	 * Constructor
	 * @param priceConfig Configuration object
	 */
	public Checkout(final JSONObject priceConfig) {
		final JSONArray items = (JSONArray) priceConfig.get("items");
		for (JSONObject item : (Iterable<JSONObject>) items) {
			final String name = (String) item.get("name");
			final long basePrice = (long) item.get("basePrice");
			final JSONObject strategy = (JSONObject) item.get("priceStrategy");
			priceItems.add(new PriceItem(name, basePrice, strategy));
		}
	}

	/**
	 * CLI entry
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1 || "--help".equals(args[0]) || "-h".equals(args[0]) || "/?".equals(args[0])) {
			printUsage();
		} else {
			final String goods = args[0];
			final Checkout co = new Checkout(readRules());
			System.out.println(String.format("%s = %s", goods, co.processItemString(goods)));
		}
	}

	/**
	 * Prints a usage help screen.
	 */
	private static void printUsage() {
		System.out.println("I calculate the total price of a number of items.");
		System.out.println("You can configure the items and prices in conf/prices.json");
		System.out.println("Usage: checkout.jar item1[item2[item3[...]]]");
		System.out.println("Example: checkout.jar ABACDA");
	}

	/**
	 * Adds one item to the shopping basket.
	 * @param itemName The item to add
	 */
	public void scan(final char itemName) {
		if (!itemIsValid(itemName)) {
			throw new InvalidItemException(String.format("Unknown item '%s'", itemName));
		}

		basket.add(String.valueOf(itemName));
	}

	/**
	 * Calculates a total of all items in basket.
	 * @return The total price of all items in basket
	 */
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

	/**
	 * Clears the shopping basket.
	 */
	public void clearBasket() {
		basket.clear();
	}

	/**
	 * Checks if the given itemName is valid.
	 * @param itemName Item to check
	 * @return If item is valid
	 */
	private boolean itemIsValid(final char itemName) {
		return getPriceItem(String.valueOf(itemName)) != null;
	}

	/**
	 * Gets a price item by it's name.
	 * @param itemName
	 * @return The item found or null
	 */
	private PriceItem getPriceItem(final String itemName) {
		for (PriceItem item : priceItems) {
			if (item.getName().equals(itemName)) {
				return item;
			}
		}

		return null;
	}

	/**
	 * Reads the pricing rules from the config file.
	 * @return the price config object
	 */
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

	/**
	 * Adds each item of a given item name string to the shopping basket and calculates their total.
	 * @param itemString A string of items e.g. ABACD
	 * @return the total price of given items
	 */
	public long processItemString(final String itemString) {
		for (int i = 0; i < itemString.length(); i++) {
			scan(itemString.charAt(i));
		}

		return total();
	}
}

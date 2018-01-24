import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by chris on 24.01.2018.
 */
public class Checkout {

	public class InvalidItemException extends RuntimeException {}

	Set<PriceItem> priceItems = new HashSet<>();
	CountableList<String> basket = new CountableList<>();

	public Checkout(JSONObject prices) {
		JSONArray items = (JSONArray) prices.get("items");
		for (JSONObject item : (Iterable<JSONObject>) items) {
			String name = (String) item.get("name");
			long basePrice = (long) item.get("basePrice");
			JSONObject strategy = (JSONObject) item.get("priceStrategy");
			priceItems.add(new PriceItem(name, basePrice, strategy));
		}
	}

	public void scan(char item) {
		if (!itemIsValid(item)) {
			throw new InvalidItemException();
		}

		basket.add(String.valueOf(item));
	}

	public long total() {
		long amount = 0;

		Map<String, Integer> counts = basket.getCounts();
		for (Map.Entry<String, Integer> item : counts.entrySet()) {
//			System.out.println(item.getKey() + "/" + item.getValue());
			PriceItem priceItem = getPriceItem(item.getKey());
			int itemCount = item.getValue();

			if (priceItem != null) {
				amount += priceItem.getPrice(itemCount);
			}
		}

		return amount;
	}

	public void clearBasket() {
		basket.clear();
	}

	private boolean itemIsValid(char item) {
		// todo
		return true;
	}

	private PriceItem getPriceItem(String name) {
		for (PriceItem item : priceItems) {
			if (item.getName().equals(name)) {
				return item;
			}
		}

		return null;
	}
}

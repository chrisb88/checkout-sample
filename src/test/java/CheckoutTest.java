import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

/**
 * Created by chris on 24.01.2018.
 */
public class CheckoutTest {

	@Test
	public void basePrices() {
		Checkout co = new Checkout(readRules());
		co.scan('A');
		Assert.assertEquals(50, co.total());
		co.clearBasket();

		co.scan('B');
		Assert.assertEquals(30, co.total());
		co.clearBasket();

		co.scan('C');
		Assert.assertEquals(20, co.total());
		co.clearBasket();

		co.scan('D');
		Assert.assertEquals(15, co.total());
		co.clearBasket();
	}

	@Test
	public void totals() {
		Assert.assertEquals(0, price(""));
		Assert.assertEquals(50, price("A"));
		Assert.assertEquals(80, price("AB"));
		Assert.assertEquals(115, price("CDBA"));

		Assert.assertEquals(100, price("AA"));
		Assert.assertEquals(130, price("AAA"));
		Assert.assertEquals(180, price("AAAA"));
		Assert.assertEquals(230, price("AAAAA"));
		Assert.assertEquals(260, price("AAAAAA"));

		Assert.assertEquals(160, price("AAAB"));
		Assert.assertEquals(175, price("AAABB"));
		Assert.assertEquals(190, price("AAABBD"));
		Assert.assertEquals(190, price("DABABA"));
	}

	@Test
	public void incremental() {
		Checkout co = new Checkout(readRules());
		Assert.assertEquals(0, co.total());

		co.scan('A');
		Assert.assertEquals(50, co.total());

		co.scan('B');
		Assert.assertEquals(80, co.total());

		co.scan('A');
		Assert.assertEquals(130, co.total());

		co.scan('A');
		Assert.assertEquals(160, co.total());

		co.scan('B');
		Assert.assertEquals(175, co.total());
	}

	private long price(String goods) {
		Checkout co = new Checkout(readRules());

		for (int i = 0; i < goods.length(); i++) {
			co.scan(goods.charAt(i));
		}

		return co.total();
	}

	private JSONObject readRules() {
		JSONParser parser = new JSONParser();

		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(new FileReader("./conf/prices.json"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return obj;
	}
}

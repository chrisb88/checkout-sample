import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for Checkout class.
 */
public class CheckoutTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void basePrices() {
		final Checkout co = new Checkout(Checkout.readRules());
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
		final Checkout co = new Checkout(Checkout.readRules());
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

	@Test
	public void exceptions() {
		final Checkout co = new Checkout(Checkout.readRules());

		exception.expect(Checkout.InvalidItemException.class);
		co.scan('E');
	}

	private long price(final String goods) {
		final Checkout co = new Checkout(Checkout.readRules());
		return co.processItemString(goods);
	}
}

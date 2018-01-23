import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by chris on 23.01.2018.
 */
public class HelloWorldTest {
	@Test
	public void name() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HelloWorld.print(new PrintStream(out));
		String s = out.toString();
		Assert.assertEquals("Hello World!", s);
	}
}

import java.io.PrintStream;

/**
 * Created by chris on 23.01.2018.
 */
public class HelloWorld {
	public static void main(String[] args) {
		print(System.out);
	}

	public static void print(PrintStream out) {
		out.print("Hello World!");
	}
}

import java.io.IOException;

public class Test1 {
	public static void main(String[] args) throws IOException {
		// check that asserts are enabled
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.err.println("(Java>Debug>settings: Vm Args)");
			System.exit(1);
		}

		System.out.print("testing searchPath() with 25x25 maze... ");

		Maze m25 = new Maze("src/maze25.txt");
		m25.getFirstCell().searchPath();

		Maze m25sol = new Maze("src/maze25sol.txt", false);
		assert(m25.equals(m25sol)) : "not the correct solution";

		System.out.println("\t\t[OK]");
	}
}

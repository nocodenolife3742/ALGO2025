public class Test2 {
	private static void assertPerfect(Maze m) {
		assert(m.isPerfect()) : "maze is not perfect";
	}

	public static void main(String[] args) {
		// check that asserts are enabled
		if (!Test2.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.err.println("(Java>Debug>settings: Vm Args)");
			System.exit(1);
		}

		System.out.print("testing generateRec() for a 5x5 maze... ");
		Maze m = new Maze(5, 5);
		m.getFirstCell().generateRec();
		assertPerfect(m);
		System.out.println("\t\t[OK]");

		System.out.print("testing generateRec() with more mazes of size 5x5... ");
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.getFirstCell().generateRec();
			assertPerfect(m);
		}
		System.out.println("\t[OK]");

		System.out.print("testing generateRec() for a 25x25 maze... ");
		m = new Maze(25, 25);
		m.getFirstCell().generateRec();
		assertPerfect(m);
		Maze m1 = new Maze(25, 25, false);
		m1.getFirstCell().generateRec();
		Maze m2 = new Maze(25, 25, false);
		m2.getFirstCell().generateRec();
		assert(!m1.equals(m2)) : "Maze generation is not random.";
		System.out.println("\t\t[OK]");

	}
}

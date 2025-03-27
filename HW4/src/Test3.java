import java.util.LinkedList;

public class Test3 {
	private static void assertPerfect(Maze m) {
		assert(m.isPerfect()) : "maze is not perfect";
	}

	public static void main(String[] args) {
		// check that asserts are enabled
		if (!Test3.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.err.println("(Java>Debug>settings: Vm Args)");
			System.exit(1);
		}
		
		


		System.out.print("testing generateIter() with method Newest... ");
		Maze m = new Maze(25, 25);
		m.generateIter(Bag.NEWEST);
		assertPerfect(m);
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.generateIter(Bag.NEWEST);
			assertPerfect(m);
		}
		Maze m1 = new Maze(25, 25, false);
		m1.generateIter(Bag.NEWEST);
		Maze m2 = new Maze(25, 25, false);
		m2.generateIter(Bag.NEWEST);
		assert(!m1.equals(m2)) : "Maze generation is not random.";
		System.out.println("\t\t[OK]");

		
		System.out.print("testing generateIter() with method Random... ");
		m = new Maze(25, 25);
		m.generateIter(Bag.RANDOM);
		assertPerfect(m);
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.generateIter(Bag.RANDOM);
			assertPerfect(m);
		}
		m1 = new Maze(25, 25, false);
		m1.generateIter(Bag.RANDOM);
		m2 = new Maze(25, 25, false);
		m2.generateIter(Bag.RANDOM);
		assert(!m1.equals(m2)) : "Maze generation is not random.";
		System.out.println("\t\t[OK]");

		System.out.print("testing generateIter() with method Middle... ");
		m = new Maze(25, 25);
		m.generateIter(Bag.MIDDLE);
		assertPerfect(m);
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.generateIter(Bag.MIDDLE);
			assertPerfect(m);
		}
		System.out.println("\t\t[OK]");

		System.out.print("testing generateIter() with method Oldest... ");
		m = new Maze(25, 25);
		m.generateIter(Bag.OLDEST);
		assertPerfect(m);
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.generateIter(Bag.OLDEST);
			assertPerfect(m);
		}
		System.out.println("\t\t[OK]");
		
		
		LinkedList<String> l = new LinkedList<String>();
		LinkedList<String> l2 = new LinkedList<String>();
		l.add("2");
		l.add("2");
		l.add("E");
		l.add("WS");
		l.add("E");
		l.add("WN");
		l2.add("2");
		l2.add("2");
		l2.add("S");
		l2.add("S");
		l2.add("NE");
		l2.add("NW");
		Maze mTest = new Maze(l,false);
		Maze mTest2 = new Maze(l2, false);	
		
		m = new Maze(2,2, false);
		m.generateIter(Bag.NEWEST);
		
		System.out.print("Additional test... ");
		assert(m.equals(mTest) || m.equals(mTest2)) : "Error: when a connection is created, exit the loop and return to step 1.";		
		System.out.println("\t\t\t\t\t[OK]");

	}
}

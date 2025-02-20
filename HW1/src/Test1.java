import java.util.LinkedList;
import java.util.Scanner;

public class Test1 {

	// creates a deck of cards from a string
	static Deck stringToDeck(String s) {
		Scanner sc = new Scanner(s);
		LinkedList<Integer> cards = new LinkedList<Integer>();
		while (sc.hasNextInt()) {
			cards.addLast(sc.nextInt());
		}
		sc.close();
		return new Deck(cards);
	}

	// test the method pick
	static void testPick(String i1, String i2, String o1, String o2, Integer e) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		Deck do2 = stringToDeck(o2);
		int x = di1.pick(di2);
		assert(x == e) : "\nFor the decks d1 = " + si1 + " et d2 = " + si2
				+ ", calling d1.pick(d2) should return " + e + " instead of " + x + ".";
		assert(di1.toString().equals(do1.toString())) : "\nFor the decks d1 = " + si1 + " et d2 = " + si2
				+ ", calling d1.pick(d2) should transform d1 into  " + do1 + " instead of " + di1 + ".";
		assert(di2.toString().equals(do2.toString())) : "\nFor the decks d1 = " + si1 + " et d2 = " + si2
				+ ", calling d1.pick(d2) should transform d2 into " + do2 + " instead of " + di2 + ".";
	}

	// test the method pickAll
	static void testPickAll(String i1, String i2, String o1) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		di1.pickAll(di2);
		assert(di1.toString().equals(do1.toString())) : "\nFor the decks d1 = " + si1 + " et d2 = " + si2
				+ ", calling d1.pickAll(d2) should transform d1 en " + do1 + " instead of " + di1 + ".";
		assert(di2.cards.isEmpty()) : "\ncalling d1.pickAll(d2) should empty d2.";
	}

	// test the method isValid
	static void testIsValid(int nbVals, String s, boolean b) {
		Deck d = stringToDeck(s);
		assert(d.isValid(nbVals) == b) : "\nFor the deck d = " + d + ", calling d.isValid() should return " + b
				+ ".";
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test the method pick
		System.out.print("Test of the method pick ... ");
		testPick("", "", "", "", -1);
		testPick("1 2", "", "1 2", "", -1);
		testPick("1 2 3", "4 5 6", "1 2 3 4", "5 6", 4);
		testPick("", "1", "1", "", 1);
		System.out.println("[OK]");

		// test the method pickAll
		System.out.print("Test of the method pickAll ... ");
		testPickAll("", "", "");
		testPickAll("1 1", "", "1 1");
		testPickAll("1 2 3", "4 5 6", "1 2 3 4 5 6");
		testPickAll("", "1 2 3", "1 2 3");
		System.out.println("[OK]");

		// test the method isValid
		System.out.print("Test of the method isValid ... ");
		// values ​​outside the limits
		testIsValid(1, "0", false);
		testIsValid(1, "1 1 1 2", false);
		// values ​​repeated too many times
		testIsValid(1, "1 1 1 1 1", false);
		testIsValid(3, "3 1 3 2 3 2 1 3 2 3", false);
		// valid decks
		testIsValid(1, "", true);
		testIsValid(1, "1 1", true);
		testIsValid(2, "1 1 1 2", true);
		testIsValid(3, "1 2 2 3 2 2 1 3 3", true);
		testIsValid(3, "1 3 1 3 3", true);
		System.out.println("[OK]");
	}
}

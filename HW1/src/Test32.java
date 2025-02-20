import java.util.LinkedList;
import java.util.Scanner;

public class Test32 {

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

	// test the method isOver
	static void testIsOver(String i1, String i2, boolean o) {
		Deck di1 = stringToDeck(i1);
		Deck di2 = stringToDeck(i2);
		assert(new Battle(di1, di2, new Deck()).isOver() == o) : "\nFor the battle that players have the decks " + di1 + " and " + di2 + ", calling isOver() should return " + o + ".";
	}
	
	// test the method oneRound
	static void testOneRound(String i1, String i2, String o1, String o2, boolean o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		Deck do2 = stringToDeck(o2);
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.oneRound()) : "\nFor the battle that players have the decks " + si1 + " and " + si2 + ", calling oneRound() should return " + o + ".";
		assert(do1.toString().equals(b.player1.toString())) : "\nFor the battle that players have the decks " + si1 + " et " + si2 + ", calling oneRound() should transform the deck of the first player to " + do1 + " rather than " + b.player1 + ".";
		assert(do2.toString().equals(b.player2.toString())) : "\nFor the battle that players have the decks " + si1 + " et " + si2 + ", calling oneRound() should transform the deck of the second player to " + do2 + " rather than " + b.player2 + ".";
		assert(!o || b.trick.cards.isEmpty()) : "\nFor the battle that players have the decks " + si1 + " et " + si2 + ", the fold after calling oneRound() should be empty.";
	}
	
	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test32.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}
		
		// test of the method isOver
		System.out.print("test of the method isOver ... ");
		testIsOver("", "", true);
		testIsOver("1", "", true);
		testIsOver("", "1", true);
		testIsOver("1", "1", false);
		System.out.println("[OK]");

		// test of the method oneRound
		System.out.print("test of the method oneRound ... ");
		// not enough cards
		testOneRound("", "", "", "", false);
		testOneRound("1", "", "1", "", false);
		testOneRound("1", "1", "", "", false);
		testOneRound("1 1", "1 2", "", "", false);
		testOneRound("1 1 2 2 3 3", "1 2 2 1 3 3", "", "", false);
		// enough cards
		testOneRound("1", "2", "", "1 2", true);
		testOneRound("2", "1", "2 1", "", true);
		testOneRound("1 1 2", "1 1 3", "", "1 1 1 1 2 3", true);
		testOneRound("1 1 2 3 2 3", "1 1 2 1 3 2", "3", "2 1 1 1 1 2 2 3 1 2 3", true);
		testOneRound("1 2 3 4 1 2 3 4 1 2 3 4 4 3 2 1", "1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4", "3 2 1 1 1 2 2 3 3 4 4 1 1 2 2 3 3 4 4 1 1 2 2 3 3 4 4 4 1", "2 3 4", true);
		System.out.println("[OK]");
	}

}

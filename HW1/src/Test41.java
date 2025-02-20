import java.util.LinkedList;
import java.util.Scanner;

public class Test41 {
	
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

	// test the method game()
	static void testGame(String i1, String i2, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.game()) : "\nFor the battle that players have the decks " + si1 + " and " + si2 + ", calling game() should return " + o + ".";
	}
	
	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test23.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}
		
		// test of the method game()
		System.out.print("test of the method game() ... ");
		// equality
		testGame("", "", 0);
		testGame("1", "1", 0);
		// player1 win
		testGame("1", "", 1);
		testGame("1 1", "1", 1);		
		testGame("2", "1", 1);		
		// player2 win
		testGame("", "1", 2);
		testGame("1", "1 1", 2);
		testGame("1", "2", 2);
		// infinite part
		testGame("1 2 1 2","2 1 2 1", 3);
		System.out.println("[OK]");
	}
}

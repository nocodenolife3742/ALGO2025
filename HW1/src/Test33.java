import java.util.LinkedList;
import java.util.Scanner;

public class Test33 {

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

	// test the method winner
	static void testWinner(String i1, String i2, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.winner()) : "\nFor the battle that players have the decks " + si1 + " and " + si2 + ", calling winner() should return " + o + ".";
	}

	// test the method game(int turns)
	static void testGame(String i1, String i2, int turns, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.game(turns)) : "\nFor the battle that players have the decks " + si1 + " and " + si2 + ", calling game(" + turns + ") should return " + o + ".";
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test33.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test of the method winner
		System.out.print("test of the method winner ... ");
		// equality
		testWinner("", "", 0);
		testWinner("1", "1", 0);
		testWinner("1 1", "1 1", 0);
		// player1 win
		testWinner("1", "", 1);
		testWinner("1 1", "1", 1);
		testWinner("1 1 1", "1", 1);
		// player2 win
		testWinner("", "1", 2);
		testWinner("1", "1 1", 2);
		testWinner("1", "1 1 1", 2);
		System.out.println("[OK]");

		// test of the method game(int turns)
		System.out.print("test of the method game(int turns) ... ");
		// equality
		testGame("", "", 1, 0);
		testGame("1", "1", 1, 0);
		testGame("1 1", "1 1", 1, 0);
		// player1 win
		testGame("1", "", 1, 1);
		testGame("1 1", "1", 1, 1);
		testGame("1 1 1", "1", 1, 1);
		testGame("1 1 2", "1 1 1", 2, 1);
		// player2 win
		testGame("", "1", 1, 2);
		testGame("1", "1 1", 1, 2);
		testGame("1", "1 1 1", 1, 2);
		testGame("1 1 1 3 3", "1 1 2", 5, 2);

		// a somewhat special battle
		testGame("1 2 1 2","2 1 2 1", 1, 2);
		testGame("1 2 1 2","2 1 2 1", 2, 0);
		testGame("1 2 1 2","2 1 2 1", 3, 2);
		testGame("1 2 1 2","2 1 2 1", 4, 0);
		testGame("1 2 1 2","2 1 2 1", 5, 1);
		testGame("1 2 1 2","2 1 2 1", 6, 0);
		testGame("1 2 1 2","2 1 2 1", 7, 1);
		testGame("1 2 1 2","2 1 2 1", 8, 0);
		testGame("1 2 1 2","2 1 2 1", 9, 1);
		testGame("1 2 1 2","2 1 2 1", 10, 0);
		testGame("1 2 1 2","2 1 2 1", 11, 1);
		System.out.println("[OK]");

	}
}

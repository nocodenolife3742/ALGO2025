
public class Test31 {

	// testing the Battle constructor
	static void testBattle(int nbVals) {
		Battle b = new Battle(nbVals);
		// we check that each player has 2*nbVals cards
		assert (b.player1.cards.size() == 2 * nbVals && b.player1.cards.size() == 2 * nbVals)
				: "\n After calling the Battle(" + nbVals + "), every player should have " + (2 * nbVals) + " carts.";
		// we rebuild the complete deck and verify that it is valid
		Deck d = new Deck();
		d.pickAll(b.player1.copy());
		d.pickAll(b.player2.copy());
		assert (d.cards.size() == 4 * nbVals && d.isValid(nbVals)) : "\nThe cards are badly distributed.";
		// we check that the cards have been mixed before being distributed
		// for this we check that we cannot obtain the arranged deck from player1 and
		// player2 in 4 different ways
		String tri = new Deck(nbVals).toString();
		// * first player1, then player2 (already built)
		assert (!d.toString().equals(tri)) : "\nThe cards were not shuffled before the distribution.";
		// * first player2, then player1
		d = new Deck();
		d.pickAll(b.player2.copy());
		d.pickAll(b.player1.copy());
		assert (!d.toString().equals(tri)) : "\nThe cards were not shuffled before the distribution.";
		// * alternating player1 then player2
		d = new Deck();
		for (int i = 0; i < 2 * nbVals; i++) {
			d.pick(b.player1);
			d.pick(b.player2);
		}
		assert (!d.toString().equals(tri)) : "\nThe cards were not shuffled before the distribution.";
		// * alternating player2 then player1
		d = new Deck();
		for (int i = 0; i < 2 * nbVals; i++) {
			d.pick(b.player2);
			d.pick(b.player1);
		}
		assert (!d.toString().equals(tri)) : "\nThe cards were not shuffled before the distribution.";
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test of the constructor of Battle
		System.out.print("test of the constructor of Battle ... ");
		testBattle(13);
		testBattle(20);
		System.out.println("[OK]");
	}
}

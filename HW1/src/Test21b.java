
public class Test21b {

	// test the method split
	static void testSplit(Deck d) {
		Deck r = d.copy();
		Deck l = r.split();
		// checks that the first deck is not empty
		assert (!l.cards.isEmpty())
				: "\nThe deck returned by the method split is empty. This is possible but unlikely. Retry the test.";
		// verifies that the deck returned by the method split is the first part of the
		// packet
		while (!l.cards.isEmpty())
			assert (l.cards.removeFirst() == d.cards.removeFirst())
					: "\nThe split method should return the first part of the deck.";
		// checks that the deck left after the split method is the second part of the
		// packet
		while (!r.cards.isEmpty())
			assert (r.cards.removeFirst() == d.cards.removeFirst())
					: "\nThe split method should remove the first part of the packet.";
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test21b.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test of the method split
		System.out.print("Test of the method split ... ");
		for (int i = 0; i < 100; i++)
			testSplit(new Deck(13));
		System.out.println("[OK]");
	}
}

import java.util.HashSet;

public class Test23 {

	// returns the number of consecutive subarrays of t of size a containing only b
	// distinct elements
	static int countFewValues(Object[] t, int a, int b) {
		int count = 0;
		HashSet<Object> values = new HashSet<Object>();
		for (int i = 0; i < t.length - (a - 1); i++) {
			values.clear();
			for (int k = 0; k < a; k++)
				values.add(t[i + k]);
			count += (values.size() <= b ? 1 : 0);
		}
		return count;
	}

	// tests whether a deck is poorly shuffled (ie. contains at least 14 pairs or 3
	// quadruplets, or 2 octuplets).
	static boolean isPoorlyMixed(Deck d) {
		Object[] t = d.cards.toArray();
		return countFewValues(t, 2, 1) >= 14 || countFewValues(t, 4, 1) >= 3 || countFewValues(t, 8, 2) >= 2;
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test23.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test of the method riffleShuffle
		System.out.print("test of the method riffleShuffle ... (may take up to 1 minute) ... ");
		int countPoorlyMixed = 0;
		for (int i = 0; i < 1000000; i++) {
			Deck d = new Deck(13);
			d.riffleShuffle(7);
			if (isPoorlyMixed(d))
				countPoorlyMixed++;
			assert (countPoorlyMixed < 5) : "\nThe shuffle done by riffleShuffle looks bad.";
		}
		System.out.println("[OK]");
	}

}

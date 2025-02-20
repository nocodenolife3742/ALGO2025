import java.util.LinkedList;
import java.util.HashSet;

public class Test22 {

	static void testOrderPreservation(int timeout) {
		for (int k = 1; k <= timeout; k++) {
			int N = 26;
			LinkedList<Integer> l1 = new LinkedList<Integer>();
			LinkedList<Integer> l2 = new LinkedList<Integer>();
			for (int j = 1; j <= N; j++) {
				l1.add(j);
				l2.add(j + N);
			}
			Deck d1 = new Deck(l1); // the Deck d1 contains 1, 2, ..., N
			Deck d2 = new Deck(l2); // the Deck d1 contains N+1, N+2, ...; 2N
			Deck d1copy = d1.copy();
			Deck d2copy = d2.copy();
			d1.riffleWith(d2); // d1 and d2 are destroyed, d1 contains the result of the “riffle”
			Deck e1 = new Deck();
			Deck e2 = new Deck();
			// Extracting the sub-deck of cards of value <= N,
			// and that of cards of value > N.
			for (Integer card : d1.cards) {
				if (card <= N) {
					e1.cards.addLast(card);
				} else {
					e2.cards.addLast(card);
				}
			}
			assert (e1.equals(d1copy) && e2.equals(d2copy)) : "\nFor the decks\nd1 = " + d1copy + "\nand\nd2 = "
					+ d2copy + "\nthe return of the method d1.riffleWith(d2) should not be\n" + d1
					+ "\nbecause at least one of the extracted decks\n" + e1 + "\nand\n" + e2
					+ "\nis not increasing (or missing cards).";
		}
	}

	private static int binomial(int a, int b) {
		if (a > b || a < 0)
			return 0;
		if (a == 0 || a == b)
			return 1;
		return binomial(a - 1, b - 1) + binomial(a, b - 1);
	}

	static void testRandomness(int timeout) {
		int N = 5;
		int nbOutputs = binomial(N, 2 * N);
		HashSet<Deck> r = new HashSet<Deck>();
		int counter = 0;
		r = new HashSet<Deck>();
		while (r.size() < nbOutputs && counter <= timeout) {
			counter++;
			LinkedList<Integer> l1 = new LinkedList<Integer>();
			LinkedList<Integer> l2 = new LinkedList<Integer>();
			for (int j = 1; j <= N; j++) {
				l1.add(j);
				l2.add(j + N);
			}
			Deck d1 = new Deck(l1); // the Deck d1 contains 1, 2, ..., N
			Deck d2 = new Deck(l2); // the Deck d1 contains N+1, N+2, ...; 2N
			d1.riffleWith(d2); // there are (N out of 2N) possible outcomes (but not uniformly distributed)
			r.add(d1);
		}
		assert (r.size() == nbOutputs) : "\nAfter " + timeout
				+ " testing of the method RiffleWith with the decks 1,2,3,4,5 and 6,7,8,9,10 "
				+ "one of the " + nbOutputs + " possible results never appeared: it is very unlikely, \n"
				+ "	your implementation of the method RiffleWith probably does not respect the probabilistic constraint.";
		System.out.println("[OK]" + " (" + counter + " tests to cover the " + nbOutputs + " possibilities)");
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test22.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		// test of the method riffleWith
		System.out.print("Test of the method riffleWith ... ");
		testOrderPreservation(10000);
		testRandomness(10000);
	}
}


public class Test21a {

	// calculates the deviation of an array from the binomial distribution
	static double deviation(double[] hist) {
		int n = hist.length-1;
		double max = 0;
		for (int i = 0; i <= n; i++) {
			// calculation of the binomial coefficient (n,i)
			double coeff = 1;
			for (int j = 1; j <= i; j++)
				coeff *= (double) (n + 1 - j) / j;
			coeff /= (long) 1 << n;
			// comparison with hist[i]
			max = Math.max(max, Math.abs(coeff - hist[i]));
		}
		return max;
	}

	public static void main(String[] args) {

		// checks that asserts are enabled
		if (!Test21a.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.err.println("See the 'Enable assert' section of the HW preamble.");
			System.exit(1);
		}

		System.out.print("Test of the method cut ... ");
		int n = 52; // game size
		int m = 100000; // number of random cuts
		double[] hist = new double[n + 1];
		Deck d = new Deck(13);
		// calculates m random cuts using the method cut
		for (int i = 0; i < m; i++) {
			int c = d.cut();
			assert(0 <= c && c < n) : "\nThe call of cut should return a number between 0 (include) and " + n + " (exclude).";
			hist[c] += 1./m;
		}
		// calculates the deviation from the binomial distribution in sup norm
		double max = deviation(hist);
		assert(max < 0.003) : ("\nFor n = " + n + " and m = " + m
				+ ", your method cut gives a deviation in sup norm of " + max + " which exceeds 0.003.\n"
				+ "There is probably a problem.");
		assert(max < 0.0025) : ("\nPour n = " + n + " et m = " + m
				+ ", your method cut gives a deviation in sup norm of " + max + " which exceeds 0.0025.\n"
				+ "It is possible but unlikely. Try the test again..");
		System.out.println("[OK]");
	}
}

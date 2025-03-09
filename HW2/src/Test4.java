public class Test4 {

	// test the method count of CountConfigurationsHashTable
	static void testCount(int n, long o) {
		System.out.print("    Compute the number of grids of size " + n + "x" + n + " ... ");
		long startTime = System.nanoTime();
		long res = CountConfigurationsHashTable.count(n);
		long endTime = System.nanoTime();
		System.out.println(
				res + " (time of calculating : " + String.format("%.2f", (endTime - startTime) / 1000000.0) + " ms)");
		assert (res == o) : "\nThere are " + o + " stable configurations of size " + n + "x" + n + ".";
	}

	public static void main(String[] args) {

		// check that asserts are enabled
		if (!Test4.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.exit(1);
		}

		// test of the method count of CountConfigurationsHashTable
		System.out.println("Test of the method count(int n) of CountConfigurationsHashTable ... ");
		long[] nums = new long[] { 1L, 2L, 16L, 102L, 2030L, 60232L, 3858082L, 446672706L, 101578277384L,
				43680343039806L, 36133311325799774L };
		for (int n = 0; n <= 10; ++n) {
			testCount(n, nums[n]);
		}
		System.out.println("[OK]");
	}
}

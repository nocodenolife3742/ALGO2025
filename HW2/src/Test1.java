import java.util.LinkedList;

public class Test1 {

	// create a row of fruits from a string
	static Row stringToRow(String s) {
		int[] fruits = new int[s.length()];
		for (int i = 0; i < s.length(); i++)
			fruits[i] = (s.charAt(i) == '0' ? 0 : 1);
		return new Row(fruits);
	}

	// test of the method extendedWith
	// we suppose that si + "f" and so have the same length
	static void testExtendedWith(String si, int f, String so) {
		assert (stringToRow(si).extendedWith(f).equals(stringToRow(so))) : "\nLa ligne\n" + si
				+ "After the call of extendedWith(" + f + ") should be the row \n" + so + ".";
	}

	// test of the method allStableRows
	static void testAllStableRows(int n, int r) {
		int x = Row.allStableRows(n).size();
		assert (x == r) : "\nThere are " + r + " stable rows of width " + n
				+ " (your method allStableRows finds " + x + ").";
	}

	// test of the method areStackable
	static void testAreStackable(String s1, String s2, String s3, boolean e) {
		assert (e == stringToRow(s1).areStackable(stringToRow(s2), stringToRow(s3))) : "\nThe rows\n" + s1 + "\n" + s2
				+ "\n" + s3 + "\n" + (e ? "should " : "should not ") + "be stackable.";
	}

	public static void main(String[] args) {

		// check that asserts are enabled
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.exit(1);
		}

		// test of the method extendedWith
		System.out.print("Test of the method extendedWith ... ");
		testExtendedWith("", 0, "0");
		testExtendedWith("", 1, "1");
		testExtendedWith("011", 0, "0110");
		testExtendedWith("0100" + "1", 0, "010010");
		testExtendedWith("100110", 1, "1001101");
		System.out.println("[OK]");

		// test of the method allStableRows
		System.out.println("Test of the method allStableRows :");
		int[] nums = new int[] { 1, 2, 4, 6, 10, 16, 26, 42, 68, 110, 178, 288, 466, 754, 1220, 1974, 3194, 5168, 8362,
				13530 };

		// test of the number of configurations for widths between 0 and 20
		System.out.print("Test of the number of stable rows ...");
		for (int n = 0; n < 20; n++)
			testAllStableRows(n, nums[n]);
		System.out.println("[OK]");	

		System.out.print("Test of configuration of widths 0,1,2...");
		//System.out.print("Test of the stable rows of width 0...");
		LinkedList<Row> n0 = Row.allStableRows(0);
		assert (n0.contains(stringToRow(""))) : "The stable row of width 0 generated is not the right one \n";
		//System.out.println("[OK]");	
		
		//System.out.print("test of the stable rows of width 1...");
		LinkedList<Row> n1 = Row.allStableRows(1);
		assert(n1.contains(stringToRow("0")) && n1.contains(stringToRow("1"))) : "The stable rows of width 1 generated are not correct \n";
		//System.out.println("[OK]");	
		
		//System.out.print("Test of the stable rows of width 2...");
		LinkedList<Row> n2 = Row.allStableRows(2);
		assert(n2.contains(stringToRow("00")) && n2.contains(stringToRow("01")) && n2.contains(stringToRow("10")) && n2.contains(stringToRow("11")) ) : 	
			"The stable rows of width 2 generated are not correct";	
		
		System.out.println("[OK]");	
	

		// test of the method areStackable
		System.out.print("Test of the method areStackable ... ");
		// different size
		testAreStackable("1010", "011", "100", false);
		// test of the first and last column (in case a loop starts at 1 instead of 0)
		testAreStackable("1", "1", "1", false);
		testAreStackable("0", "0", "0", false);
		// other examples
		testAreStackable("1011", "0110", "1100", true);
		testAreStackable("1011", "0110", "1101", true);
		testAreStackable("0001", "0110", "1100", true);
		testAreStackable("1101", "1110", "1100", false);
		testAreStackable("101011", "011011", "110011", false);
		testAreStackable("101", "011", "111", false);
		System.out.println("[OK]");
	}
}

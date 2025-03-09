import java.util.LinkedList;

public class Test3 {

	// test the HashTable constructor
	static void testHashTable() {
		HashTable t = new HashTable();
		assert (t.buckets != null) : "\nBuckets field was not initialized in HashTable constructor.";
		assert (t.buckets.size() == HashTable.M) : "\nThe bucket vector should have size " + HashTable.M
				+ ". Currently, its capacity is " + t.buckets.capacity() + " and its size is " + t.buckets.size()
				+ ".";
		for (LinkedList<Quadruple> bucket : t.buckets) {
			assert (bucket != null
					&& bucket.equals(new LinkedList<>())) : "\nEach bucket box must contain an empty list.";
		}
	}

	// test the add and find methods
	static void testAddFind(HashTable t, Row r1, Row r2, int height, long result, boolean alreadyIn) {
		if (alreadyIn) {
			// we check that (r1, r2, height) is present in the table t
			assert (t.find(r1, r2, height) != null) : "\nThe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") should be in the table.";
			// we check that the first search did not remove the entry
			assert (t.find(r1, r2, height) != null) : "\nThe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") should be in the table.";
			// we check that the associated result is correct
			assert (t.find(r1, r2, height).equals(result)) : "\nThe result associates with the triplet (" + r1 + ", " + r2
					+ ", " + height + ") should be " + result + ".";
		} else {
			// otherwise, we check that (r1, r2, height) is not present in the table t
			assert (t.find(r1, r2, height) == null) : "\nThe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") should not be in the table.";
			// we add (r1, r2, height, result) to the table t
			t.add(r1, r2, height, result);
			// and we check that it is in the table t
			testAddFind(t, r1, r2, height, result, true);
		}
	}

	public static void main(String[] args) {

		// check that asserts are enabled
		if (!Test3.class.desiredAssertionStatus()) {
			System.err.println("You must pass the option -ea to the virtual machine Java.");
			System.exit(1);
		}
		// test of the HashTable constructor
		System.out.print("Test of the constructor of HashTable ... ");
		testHashTable();
		System.out.println("[OK]");

		// all words on 0/1 of size at most 10
		Row[] rows = new Row[512];
		for (int i = 0; i < 512; i++) {
			int[] bits = new int[10];
			for (int j = 0; j < 10; j++)
				bits[j] = (i >> j) & 1;
			rows[i] = new Row(bits);
		}

		HashTable t = new HashTable();

		// test of the hashCode and bucket methods
		System.out.print("Test of methods hashCode and bucket ... ");
		int[] count = new int[HashTable.M];
		// we calculate the bucket of many triplets ...
		for (int i1 = 0; i1 < 512; i1++)
			for (int i2 = 0; i2 < 512; i2++)
				for (int height = 0; height < 100; height++) {
					int b = t.bucket(rows[i1], rows[i2], height);
					assert (0 <= b && b < HashTable.M) : "\nThe method bucket should return a number between 0 and "
							+ HashTable.M + ".";
					count[b]++;
				}
		// ... and we check that they are well distributed between 0 and M
		int nbZeros = 0;// number of buckets used
		int mini = Integer.MAX_VALUE;
		int maxi = 0;
		for (int i = 0; i < HashTable.M; i++) {
			nbZeros += (count[i] == 0 ? 1 : 0);
			mini = Math.min(mini, count[i]);
			maxi = Math.max(maxi, count[i]);
		}
		assert (2 * nbZeros < HashTable.M) : "\nThe method bucket don't even use half the table.";
		assert (maxi < 1000 * (mini + 1)) : "\nThe method bucket uses one bucket 1000 times more than another.";
		System.out.println("[OK]");

		// test of the add and find methods
		System.out.print("Test of methods add and find ... ");
		// we start with a particular case
		Row r1 = new Row(new int[] { 0, 0, 1, 0, 1 });
		Row r2 = new Row(new int[] { 1, 0, 1, 0, 1 });
		testAddFind(t, r1, r2, 2, 23, false);
		// another particular case
		Row r3 = new Row(new int[] { 0, 0, 1, 1, 0 });
		Row r4 = new Row(new int[] { 0, 1, 1, 1, 0 });
		testAddFind(t, r3, r4, 3, 42, false);
		// we check that we use equals and not ==
		Row r3bis = new Row(new int[] { 0, 0, 1, 1, 0 });
		Row r4bis = new Row(new int[] { 0, 1, 1, 1, 0 });
		testAddFind(t, r3bis, r4bis, 3, 42, true);
		// test of the add and find methods with many collisions
		for (int i1 = 0; i1 < 512; i1++)
			for (int i2 = 0; i2 < 512; i2++)
				testAddFind(t, rows[i1], rows[i2], i1 * i2, 0L, false);
		System.out.println("[OK]");
	}
}

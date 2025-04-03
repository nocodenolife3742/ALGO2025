import java.util.Arrays;

public class TestCompare {

	public static void main(String[] args) {

		if (!TestCompare.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.exit(1);
		}

		// test de KDtree.compare
		System.out.println("--Test of the method compare ...");
		
		double[][] testPoints = {
				{0., 0., 0.},
				{-.42, 7.5765, -3.7}
		};
		
		for (double[] a : testPoints) {
			for (int d = 0; d < 3*a.length; d++) {
				assert a.length >= 2;
				KDTree kd = new KDTree(a, d);
				double[] b = a.clone();
				assert kd.compare(b):
					String.format("t.compare(%s) for a subtree t from root %s to depth %d should return true",
							Arrays.toString(b), Arrays.toString(a), d);
				b[d % a.length] += 1.;
				assert kd.compare(b):
					String.format("t.compare(%s) for a subtree t from root %s to depth %d should return true",
							Arrays.toString(b), Arrays.toString(a), d);
				b[(d + 1) % a.length] += 1.;
				assert kd.compare(b):
					String.format("t.compare(%s) for a subtree t from root %s to depth %d should return true",
							Arrays.toString(b), Arrays.toString(a), d);
				b[d % a.length] -= 2.;
				assert !kd.compare(b):
					String.format("t.compare(%s) for a subtree t from root %s to depth %d should return false",
							Arrays.toString(b), Arrays.toString(a), d);
			}
		}

		System.out.println("[OK]");

	}
}

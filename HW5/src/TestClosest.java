import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public abstract class TestClosest {

	String name;

	abstract double[] closest(KDTree tree, double[] point);

	static String pointToString(KDTree tree, double[] point) {
		String res = String.format(Locale.ROOT, "[%f, %f, %f]", point[0], point[1], point[2]);
		if (tree != null) {
			if (Arrays.equals(point, tree.point))
				res += " (= root of the tree)";
			else if (tree.left != null && Arrays.equals(point, tree.left.point))
				res += " (= tree.left.point)";
			else if (tree.right != null && Arrays.equals(point, tree.right.point))
				res += " (= tree.right.point)";
		}
		return res;
	}

	public void testClosest(int size, int runs) {

		if (!TestClosest.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// we fill a 3d tree with the colors of pixels of an image,
		// and we get a subset of the same points to use in the tests
		Picture pic = new Picture("photo.jpg");
		int height = pic.height();
		int width = pic.width();
		int stride = height * width / size;
		assert size >= 0 && stride > 0 : "Parameter size incorrect";
		assert runs <= size : "runs should be <= size";
		int testStride = (size - 3) / (runs - 3);

		KDTree tree = null;
		Vector<double[]> testPoints = new Vector<double[]>();
		for (int i = 0; i < size; i++) {
			int c = pic.getRGB((i * stride) % width, i * stride / width);
			double[] point = new double[3];
			point[0] = (c >> 16) & 255;
			point[1] = (c >> 8) & 255;
			point[2] = c & 255;
			tree = KDTree.insert(tree, point);
			if (i < 3 || (i - 3) % testStride == 0 && testPoints.size() < runs)
				testPoints.add(point);
		}
		
		assert TestInsert.size(tree) == size: "impossible to build the test tree (insert() method incorrect?)";

		System.out.printf("--Test of the method %s (%d points, 16*%d tests)...\n", name, size, runs);

		long startTime = System.currentTimeMillis();

		for (double[] a : testPoints) {
			// we use here the fact that by construction, the points of
			// our test tree have integer coordinates
			for (int i = 0; i < 8; i++) {
				double[] b = a.clone();
				for (int j = 0; j < 3; j++)
					if ((i & (1 << j)) != 0)
						b[j] += .1;
				double[] c = closest(tree, b);
				assert (Arrays.equals(a, c)) : String.format("the nearest point of %s should be %s and not %s",
						pointToString(tree, b), pointToString(tree, a), pointToString(tree, c));
			}
		}
		
		// second test with slightly larger perturbations
		Random rnd = new Random(0);
		for (double[] a : testPoints) {
			for (int i = 0; i < 8; i++) {
				double[] b = a.clone();
				for (int j = 0; j < 3; j++)
					b[j] += rnd.nextGaussian()/2;
				double[] c = closest(tree, b);
				assert (KDTree.sqDist(c, b) <= KDTree.sqDist(a, b)) : String.format("The nearest point of %s is not %s (%s is the closest)",
						pointToString(tree, b), pointToString(tree, c), pointToString(tree, a));
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.printf("Total time : %f sec.\n", (endTime - startTime) / 1000.);
		System.out.println("[OK]");

	}
}

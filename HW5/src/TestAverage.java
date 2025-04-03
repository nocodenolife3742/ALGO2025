import java.util.Random;

public class TestAverage {

	static void testSize(KDTree inputTree, int output) {
		assert (output == KDTree.size(inputTree)) : " the number of nodes should be " + output;

	}

	static void testSum(KDTree inputTree, double[] inputAcc, double[] outputAcc) {

		KDTree.sum(inputTree, inputAcc);

		for (int i = 0; i < inputTree.point.length; i++) {
			assert (Math.abs(outputAcc[i] - inputAcc[i]) < 1e-9) : "Error in sum calculation";
		}

	}

	static void testAverage(KDTree inputTree, double[] output) {

		double[] acc = new double[inputTree.point.length];

		KDTree.sum(inputTree, acc);
		int size1 = KDTree.size(inputTree);
		for (int i = 0; i < inputTree.point.length; i++) {
			assert (Math.abs(output[i] - acc[i] / size1) < 1e-9) : "Error in calculating the average at"
					+ " coefficient" + i;
		}

	}

	public static void main(String[] args) {

		if (!TestAverage.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		Picture pic = new Picture("photo.jpg");
		// pic.show();

		Random r = new Random();

		KDTree tree = null;
		int iter = 20000;
		int size = KDTree.size(tree);

		assert (size == 0) : "the number of nodes of tree is 0";

		for (int i = 0; i < iter; i++) {
			int row = r.nextInt(pic.height()), col = r.nextInt(pic.width());
			int c = pic.getRGB(col, row);

			// construction of the node labeled by a point which contains the 3 color coordinates
			double[] point = new double[3];
			point[0] = (c >> 16) & 255;
			point[1] = (c >> 8) & 255;
			point[2] = c & 255;
			tree = KDTree.insert(tree, point);
		}
		System.out.println("--Test of the method size ... ");

		testSize(tree, 20000);
		double[] p0 = { -2.0, 0.0, 0.0 };
		KDTree treeTest = KDTree.insert(null, p0);
		testSize(treeTest, 1);
		double[] p2 = { -2.0, 0.0, 0.0 };
		double[] p1 = { -1.0, -1.0, 0.0 };

		treeTest = KDTree.insert(treeTest, p1);

		testSize(treeTest, 2);

		System.out.println("--Test of the method sum ... ");

		treeTest = KDTree.insert(treeTest, p2);

		double[] inacc1 = new double[treeTest.point.length];

		double[] outacc1 = new double[] { -5.0, -1.0, 0.0 };
		testSum(treeTest, inacc1, outacc1);

		System.out.println("--Test of the method average ... ");

		double[] averagepoint = KDTree.average(treeTest);
		testAverage(treeTest, averagepoint);

		double p3[] = { 0.0, 0.5, 0.0 };

		KDTree treeTest1 = new KDTree(p3, 1);
		double p4[] = { -1.0, -1.0, -1.0 };
		double p5[] = { -1.0, 6.0, 0.5 };

		treeTest1 = KDTree.insert(treeTest1, p3);
		treeTest1 = KDTree.insert(treeTest1, p4);
		treeTest1 = KDTree.insert(treeTest1, p5);

		double[] inacc2 = new double[treeTest1.point.length];

		double[] outacc2 = new double[] { -2.0, 6.0, -0.5 };

		testSum(treeTest1, inacc2, outacc2);

		averagepoint = KDTree.average(treeTest1);
		testAverage(treeTest1, averagepoint);

		System.out.println("[OK]");
	}

}

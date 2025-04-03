
public class TestInsert {
	
	public static int size(KDTree tree) {
		if (tree == null)
			return 0;
		return 1 + size(tree.left) + size(tree.right);
	}

	static double p [][] = {
			{ 0.0, 0.0, 0.0 },
			{ -1.0, 0.0, 0.0 },
			{ 1.0, 0.0, 0.0 },
			{ 0.0, 2.0, 0.0 },
			{ -1.0, -1.0, -1.0 }
	};

	static KDTree do_insert(KDTree kd, int i) {
		System.out.printf("kd = insert(%s, p%d = [%f, %f, %f]);\n",
				(kd == null ? "null" : "kd"),
				i, p[i][0], p[i][1], p[i][2]);
		int size_before = size(kd);
		kd = KDTree.insert(kd, p[i]);
		int size_after = size(kd);
		assert (size_after == size_before + 1):
			String.format("size before last insertion = %d, after = %d", size_before, size_after);
		return kd;
	}

	public static void main(String[] args) {

		if (!TestInsert.class.desiredAssertionStatus()) {
			System.err.println("You must pass the -ea option to the Java Virtual Machine.");
			System.exit(1);
		}

		// test de KDtree.insert
		System.out.println("--Test of the method insert");

		KDTree kd = do_insert(null, 0);
		assert (kd != null) : "...should return a new KDTree containing p at depth 0 but returned null";

		kd = do_insert(kd, 1);
		assert (kd.left != null) : "...devrait renvoyer un KDTree tel que kd.left!=null";
		assert (kd.left.point.equals(p[1])) : "kd should contain point p1 in the left subtree";
		
		kd = do_insert(kd, 2);
		assert (kd.right != null) : "kd.right should be non-null";
		assert (kd.right.point.equals(p[2])
				&& kd.right.depth == 1) : "kd.right should be p2 at depth 1";
		
		kd = do_insert(kd, 4);
		assert (kd.left.left != null) : "kd.left.left should be != null";
		assert (kd.left.left.point.equals(p[4])
				&& kd.left.left.depth == 2) : "kd.left.left.point should be p4 at depth 2";

		kd = do_insert(kd, 3);
		kd = do_insert(kd, 4);
		assert (kd.right != null) : "kd.right should be != null";
		assert (kd.right.point.equals(p[2])
				&& kd.right.depth == 1) : "kd.right should be p2 at depth 1";
		assert (kd.right.right != null) : "kd.right.right should be != null";
		assert (kd.right.right.point.equals(p[3])	&& kd.right.right.depth == 2) :
			"kd.right.right should be p3 at depth 2,"
						+ " we have kd.right.right.depth=" + kd.right.right.depth;

		System.out.println("[OK]");
	}

}

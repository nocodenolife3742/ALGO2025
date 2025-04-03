public class TestClosestOptimized extends TestClosest {

	TestClosestOptimized() {
		name = "closest() [optimized version]";
	}

	double[] closest(KDTree tree, double[] a) {
		return KDTree.closest(tree, a);
	}

	public static void main(String[] args) {
		TestClosestOptimized test = new TestClosestOptimized();
		test.testClosest(10, 10);
		test.testClosest(100, 100);
		test.testClosest(20000, 1000);
		System.out.println("the following test may take a few seconds");	
		test.testClosest(100000, 100000);
	}

}

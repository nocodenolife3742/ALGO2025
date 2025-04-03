import java.util.Arrays;

public class TestSqDist {

    public static void main(String[] args) {

        if (!TestSqDist.class.desiredAssertionStatus()) {
            System.err.println("You must pass the -ea option to the Java Virtual Machine.");
            System.exit(1);
        }

        // Test KDtree.sqDist
        System.out.println("--Test of the method sqDist ...");

        // Test cases for sqDist method
        double[][][] testPairs = {
                // Basic 2D test cases
                {{0., 0.}, {3., 4.}},
                {{0., 0.}, {-3., -4.}},
                {{1., 2.}, {1., 2.}},
                {{5., 5.}, {10., 10.}},
                {{-5., -5.}, {5., 5.}},

                // 3D test cases
                {{0., 0., 0.}, {1., 2., 2.}},
                {{3., 4., 5.}, {6., 8., 10.}},
                {{1., 2., 3.}, {4., 5., 6.}},

                // High-dimensional test cases
                {{1., 2., 3., 4.}, {5., 6., 7., 8.}},
                {{1., 1., 1., 1., 1., 1.}, {2., 2., 2., 2., 2., 2.}},
                {{0., 0., 0., 0., 0., 0., 0., 0., 0., 0.}, {1., 1., 1., 1., 1., 1., 1., 1., 1., 1.}}
        };
        double[] expectedResults = {
                25., 25., 0., 50., 200., // Basic 2D test cases
                9., 50., 27., // 3D test cases
                64., 6., 10. // High-dimensional test cases
        };

        // Loop through each test case
        for (int i = 0; i < testPairs.length; i++) {
            // Extract the points from the test case
            // and the expected result
            double[] a = testPairs[i][0];
            double[] b = testPairs[i][1];
            double expected = expectedResults[i];

            // Calculate the squared distance
            double result = KDTree.sqDist(a, b);

            // Check if the result is as expected
            // Use a small epsilon to account for floating point precision
            assert Math.abs(result - expected) < 1e-10 : String.format("sqDist(%s, %s) should be %f but was %f",
                    Arrays.toString(a), Arrays.toString(b), expected, result);
        }

        System.out.println("[OK]");
    }

}

import java.util.Vector;

public class KDTree {
    int depth;
    double[] point;
    KDTree left;
    KDTree right;

    KDTree(double[] point, int depth) {
        this.point = point;
        this.depth = depth;
    }

    boolean compare(double[] a) {
        // check if the dimension of the point
        // is the same as the dimension of a
        assert (a.length == point.length);

        // compare the dimension of the point
        int dimension = depth % point.length;
        return a[dimension] >= point[dimension];
    }

    static KDTree insert(KDTree tree, double[] p) {
        // check if tree is empty
        if (tree == null)
            return new KDTree(p, 0);

        // compare the point with the tree and
        // insert it in the right or left subtree
        KDTree leaf = new KDTree(p, tree.depth + 1);
        if (tree.compare(p))
            tree.right = tree.right == null ? leaf : insert(tree.right, p);
        else
            tree.left = tree.left == null ? leaf : insert(tree.left, p);

        // return the tree
        return tree;
    }

    static double sqDist(double[] a, double[] b) {
        // check if the dimension of the point a
        // is the same as the dimension of point b
        assert (a.length == b.length);

        // calculate the square distance
        double distance = 0;
        for (int i = 0; i < a.length; i++)
            distance += (a[i] - b[i]) * (a[i] - b[i]);

        // return the square distance
        return distance;
    }

    // Not used, so commented out
    // static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
    //     throw (new Error("TODO"));
    // }

    static double[] closestNaive(KDTree tree, double[] a) {
        // check if the tree is empty
        if (tree == null)
            return null;

        // check if the point is in the left or right subtree
        double[] left = closestNaive(tree.left, a);
        double[] right = closestNaive(tree.right, a);

        // check which point is closer to the point a
        double[] champion = tree.point;
        if (left != null && sqDist(a, left) < sqDist(a, champion))
            champion = left;
        if (right != null && sqDist(a, right) < sqDist(a, champion))
            champion = right;

        // return the closest point
        return champion;
    }

    static double[] closest(KDTree tree, double[] a, double[] champion) {
        if (tree == null)
            return champion;

        // trace the point
        InteractiveClosest.trace(tree.point, champion);

        // check if the point is in the left or right subtree
        KDTree subtree = tree.compare(a) ? tree.right : tree.left;
        KDTree other = tree.compare(a) ? tree.left : tree.right;

        // calculate the distance to the hyperplane
        int dimension = tree.depth % a.length;
        double distance = a[dimension] - tree.point[dimension];

        // check closest point in the subtree
        double[] closest = closest(subtree, a, champion);
        if (closest != null && sqDist(a, closest) < sqDist(a, champion))
            champion = closest;

        // check if the hyperplane is closer than the closest point
        if (distance * distance < sqDist(a, champion)) {

            // check point on the node
            if (sqDist(a, tree.point) < sqDist(a, champion))
                champion = tree.point;

            // check closest point in the other subtree
            closest = closest(other, a, champion);
            if (closest != null && sqDist(a, closest) < sqDist(a, champion))
                champion = closest;
        }

        // return the closest point
        return champion;
    }

    static double[] closest(KDTree tree, double[] a) {
        // check if the tree is empty
        if (tree == null)
            return null;

        // return the closest point
        return closest(tree, a, tree.point);
    }

    static int size(KDTree tree) {
        // check if the tree is empty
        if (tree == null)
            return 0;

        // return the size of the tree
        return 1 + size(tree.left) + size(tree.right);
    }

    static void sum(KDTree tree, double[] acc) {
        // check if the tree is empty
        if (tree == null)
            return;

        // add the point to the accumulator
        for (int i = 0; i < acc.length; i++)
            acc[i] += tree.point[i];

        // sum the left and right subtrees
        sum(tree.left, acc);
        sum(tree.right, acc);
    }

    static double[] average(KDTree tree) {
        // check if the tree is empty
        if (tree == null)
            return null;

        // calculate the average of the points
        double[] acc = new double[tree.point.length];
        sum(tree, acc);
        for (int i = 0; i < acc.length; i++)
            acc[i] /= size(tree);

        // return the average point
        return acc;
    }

    // strategies for palette generation:
    //     1. choose points starting from the root
    //     2. larger trees should select more points proportionally
    static Vector<double[]> palette(KDTree tree, int maxpoints) {
        // check if the tree is empty or if maxpoints is less than or equal to 0
        if (tree == null || maxpoints <= 0)
            return new Vector<>();

        // create a vector to store the points
        Vector<double[]> points = new Vector<>();
        points.add(tree.point);
        maxpoints--;

        // distribute points between left and right subtrees proportionally
        int leftSize = size(tree.left);
        int rightSize = size(tree.right);
        int subtreesSize = leftSize + rightSize;
        int leftMaxpoints = (int) Math.round(maxpoints * ((double) leftSize / subtreesSize));
        int rightMaxpoints = maxpoints - leftMaxpoints;

        // add points from the left and right subtrees
        points.addAll(palette(tree.left, leftMaxpoints));
        points.addAll(palette(tree.right, rightMaxpoints));

        // return the vector of points
        return points;
    }

    public String pointToString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        if (this.point.length > 0)
            sb.append(this.point[0]);
        for (int i = 1; i < this.point.length; i++)
            sb.append("," + this.point[i]);
        sb.append("]");
        return sb.toString();
    }

}

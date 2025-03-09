 
/* HW2. Fruits and hash tables
 * This file contains 7 classes:
 * 		- Row represents a row of fruits,
 * 		- CountConfigurationsNaive counts stable configurations naively,
 * 		- Quadruple manipulates quadruplets,
 * 		- HashTable builds a hash table,
 * 		- CountConfigurationsHashTable counts stable configurations using our hash table,
 * 		- Triple manipulates triplets,
 * 		- CountConfigurationsHashMap counts stable configurations using the HashMap of java.
 */


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.function.BiFunction;

class Row { // represent a row of fruits
    private final int[] fruits;

    // empty row constructor
    Row() {
        this.fruits = new int[0];
    }

    // constructor from the field fruits
    Row(int[] fruits) {
        this.fruits = fruits;
    }

    // equals method to compare the row to an object o
    @Override
    public boolean equals(Object o) {
        // we start by transforming the object o into an object of the class Row
        // here we suppose that o will always be of the class Row
        Row that = (Row) o;
        // we check if the two rows have the same length
        if (this.fruits.length != that.fruits.length)
            return false;
        // we check if the i-th fruits of the two rows coincide
        for (int i = 0; i < this.fruits.length; ++i) {
            if (this.fruits[i] != that.fruits[i])
                return false;
        }
        // we have the equality of the two rows
        return true;
    }

    // hash code of the row
    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < fruits.length; ++i) {
            hash = 2 * hash + fruits[i];
        }
        return hash;
    }

    // string representing the row
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < fruits.length; ++i)
            s.append(fruits[i]);
        return s.toString();
    }

    // Question 1

    // returns a new row by adding fruit to the end of the row
    Row extendedWith(int fruit) {
        final int[] extendedFruits = new int[fruits.length + 1];
        System.arraycopy(fruits, 0, extendedFruits, 0, fruits.length);
        extendedFruits[fruits.length] = fruit;
        return new Row(extendedFruits);
    }

    // check row is stable or not
    boolean isStable() {
        for (int i = 0; i < fruits.length - 2; i++)
            if (fruits[i] == fruits[i + 1] && fruits[i + 1] == fruits[i + 2])
                return false;
        return true;
    }

    // return the list of all stable rows of width
    static LinkedList<Row> allStableRows(int width) {
        LinkedList<Row> rows = new LinkedList<>();
        rows.add(new Row());
        for (int i = 0; i < width; i++) {
            final LinkedList<Row> newRows = new LinkedList<>();
            while (!rows.isEmpty()) {
                final Row row = rows.pop();
                for (int fruit : new int[]{0, 1}) {
                    final Row extended = row.extendedWith(fruit);
                    if (extended.isStable())
                        newRows.add(extended);
                }
            }
            rows = newRows;
        }
        return rows;
    }


    // check if the row can be stacked with rows r1 and r2
    // without having three fruits of the same type adjacent
    boolean areStackable(Row r1, Row r2) {
        if (fruits.length != r1.fruits.length || fruits.length != r2.fruits.length)
            return false;
        for (int i = 0; i < this.fruits.length; i++)
            if (fruits[i] == r1.fruits[i] && fruits[i] == r2.fruits[i])
                return false;
        return true;
    }
}

// Naive counting
class CountConfigurationsNaive {  // counting of stable configurations

    // Question 2

    // returning the number of grids whose first lines are r1 and r2,
    // whose lines are lines of rows and whose height is height
    static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
        if (height <= 1) return 0;
        if (height == 2) return 1;
        long counter = 0;
        for (Row row : rows)
            if (row.areStackable(r1, r2))
                counter += count(r2, row, rows, height - 1);
        return counter;
    }

    // returning the number of grids with n lines and n columns
    static long count(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        LinkedList<Row> rows = Row.allStableRows(n);
        long counter = 0;
        for (Row row1 : rows)
            for (Row row2 : rows)
                counter += count(row1, row2, rows, n);
        return counter;
    }
}

// Construction and use of a hash table

class Quadruple { // quadruplet (r1, r2, height, result)
    Row r1;
    Row r2;
    int height;
    long result;

    Quadruple(Row r1, Row r2, int height, long result) {
        this.r1 = r1;
        this.r2 = r2;
        this.height = height;
        this.result = result;
    }
}

class HashTable { // hash table
    final static int M = 50000;
    Vector<LinkedList<Quadruple>> buckets;

    // Question 3.1

    // constructor
    HashTable() {
        buckets = new Vector<>(M);
        for (int i = 0; i < M; i++)
            buckets.add(new LinkedList<>());
    }

    // Question 3.2

    // return the hash code of the triplet (r1, r2, height)
    static int hashCode(Row r1, Row r2, int height) {
        // gcd(31, 37) = 1, so hasher is fine in this case
        // the hasher always outputs positive value
        BiFunction<Integer, Integer, Integer> hasher = (a, b) ->
                Math.toIntExact((31L * a + 37L * b) & (0x7FFFFFFF));
        int hash = hasher.apply(r1.hashCode(), r2.hashCode()); // combine r1, r2
        return hasher.apply(hash, height); // combine (r1, r2), height
    }

    // return the bucket of the triplet (r1, r2, height)
    int bucket(Row r1, Row r2, int height) {
        return hashCode(r1, r2, height) % M;
    }

    // Question 3.3

    // add the quadruplet (r1, r2, height, result) in the bucket indicated by the
    // method bucket
    void add(Row r1, Row r2, int height, long result) {
        final LinkedList<Quadruple> bucket = buckets.elementAt(bucket(r1, r2, height));
        bucket.addLast(new Quadruple(r1, r2, height, result));
    }

    // Question 3.4

    // search in the table an entry for the triplet (r1, r2, height)
    Long find(Row r1, Row r2, int height) {
        final LinkedList<Quadruple> bucket = buckets.elementAt(bucket(r1, r2, height));
        for (Quadruple q : bucket)
            if (q.r1.equals(r1) && q.r2.equals(r2) && q.height == height)
                return q.result;
        return null;
    }

}

class CountConfigurationsHashTable { // counting of stable configurations using our hash table
    static HashTable memo = new HashTable();

    // Question 4

    // return the number of grids whose first lines are r1 and r2,
    // whose lines are lines of rows and whose height is height
    // using our hash table
    static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
        if (height <= 1) return 0;
        if (height == 2) return 1;
        Long result = memo.find(r1, r2, height);
        if (result != null) return result;
        long counter = 0;
        for (Row row : rows)
            if (row.areStackable(r1, r2))
                counter += count(r2, row, rows, height - 1);
        memo.add(r1, r2, height, counter);
        return counter;
    }

    // return the number of grids with n lines and n columns
    static long count(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        LinkedList<Row> rows = Row.allStableRows(n);
        long counter = 0;
        for (Row row1 : rows)
            for (Row row2 : rows)
                counter += count(row1, row2, rows, n);
        return counter;
    }
}

// Use of HashMap

class Triple { // triplet (r1, r2, height)
    Row r1;
    Row r2;
    int height;

    Triple(Row r1, Row r2, int height) {
        this.r1 = r1;
        this.r2 = r2;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        Triple triple = (Triple) o;
        return triple.r1.equals(r1) && triple.r2.equals(r2) && triple.height == height;
    }

    @Override
    public int hashCode() {
        // same implement as HashTable
        BiFunction<Integer, Integer, Integer> hasher = (a, b) ->
                Math.toIntExact((31L * a + 37L * b) & (0x7FFFFFFF));
        int hash = hasher.apply(r1.hashCode(), r2.hashCode()); // combine r1, r2
        return hasher.apply(hash, height); // combine (r1, r2), height
    }
}

class CountConfigurationsHashMap { // counting of stable configurations using the HashMap of java
    static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

    // Question 5

    // returning the number of grids whose first lines are r1 and r2,
    // whose lines are lines of rows and whose height is height
    // using the HashMap of java
    static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
        if (height <= 1) return 0;
        if (height == 2) return 1;
        Triple triple = new Triple(r1, r2, height);
        Long result = memo.get(triple);
        if (result != null) return result;
        long counter = 0;
        for (Row row : rows)
            if (row.areStackable(r1, r2))
                counter += count(r2, row, rows, height - 1);
        memo.put(triple, counter);
        return counter;
    }

    // return the number of grids with n lines and n columns
    static long count(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        memo.clear();
        LinkedList<Row> rows = Row.allStableRows(n);
        long counter = 0;
        for (Row row1 : rows)
            for (Row row2 : rows)
                counter += count(row1, row2, rows, n);
        return counter;
    }
}

// bonus work : using dynamic programming techniques to avoid recursion and hashing
class CountConfigurationsDynamicProgramming {

    // return the number of grids with n lines and n columns
    static long count(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        final Row[] rows = Row.allStableRows(n).toArray(new Row[0]);
        final int size = rows.length;
        long[][][] memo = new long[n + 1][size][size]; // cache optimization
        // init tables
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                memo[2][i][j] = 1;
        // iterate the array
        for (int i = 3; i <= n; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    for (int l = 0; l < size; l++)
                        if (rows[j].areStackable(rows[k], rows[l]))
                            memo[i][j][k] += memo[i - 1][k][l];
        // calculate answer
        long counter = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                counter += memo[n][i][j];
        return counter;
    }
}
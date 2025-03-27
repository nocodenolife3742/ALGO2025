import java.util.LinkedList;
import java.util.Random;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


class Bag { // list of cells, with 4 different methods of accessing elements

	private LinkedList<Cell> list;
	private int method;
	private Random rnd;
	private boolean dirty;
	private int currentIndex;

	// method of selecting elements
	/** select the most recent element */
	static final int NEWEST = 0;
	/** select the oldest element */
	static final int OLDEST = 1;
	/** select the element in the middle of the list */
	static final int MIDDLE = 2;
	/** select a random element from the list */
	static final int RANDOM = 3;

	/**
	 * construct a Bag, method must be one of the 4 selection methods
	 */
	Bag(int method) {
		this.list = new LinkedList<>();
		this.method = method;

		if(method == RANDOM)
			this.rnd = new Random();

		this.dirty = true;
		this.currentIndex = -1;
	}

	/**
	 * check if the Bag is empty
	 * @return  true if the Bag is empty
	 */
	boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * add a cell to the Bag
	 */
	void add(Cell c) {
		list.addLast(c);
		dirty = true;
	}

	/**
	 * return the next element to be selected (without removing it)
	 * @return  the next element to be selected
	 */
	Cell peek() {
		return list.get(nextIndex());
	}

	// delete and return the next selected element
	/**
	 * delete and return the next selected element
	 * @return  the next selected element (and remove it)
	 */
	Cell pop() {
		Cell ret = list.get(nextIndex());
		list.remove(nextIndex());
		dirty = true;
		return ret;
	}

	private int nextIndex() {
		if(!dirty)
			return currentIndex;

		int bound = list.size();

		switch(method) {
			case NEWEST:
				currentIndex = bound-1;
				break;
			case OLDEST:
				currentIndex = 0;
				break;
			case MIDDLE:
				currentIndex = bound/2;
				break;
			case RANDOM:
				currentIndex = rnd.nextInt(bound);
				break;
			default:
				throw new IllegalStateException("invalid selection method");
		}

		dirty = false;
		return currentIndex;
	}
}


class UnionFind { // disjoint classes
	private int[] link; // array of links
	private int[] rank; // array of ranks
	private int numClasses; // number of classes
	private int[] size; // the array of class sizes

	// constructor
	UnionFind(int n) {
		if (n < 0) throw new IllegalArgumentException();
		this.link = new int[n];
		for (int i = 0; i < n; i++) this.link[i] = i;
		this.rank = new int[n];
		this.numClasses = n;
		this.size = new int[n];
		for (int i = 0; i < n; i++) this.size[i] = 1;
	}

	// returns the number of classes
	int numClasses() {
		return this.numClasses;
	}

	// return the representative of an element i
	int find(int i) {
		if (i < 0 || i >= this.link.length)
			throw new ArrayIndexOutOfBoundsException(i);
		int p = this.link[i];
		if (p == i) return i;
		int r = this.find(p);
		this.link[i] = r; // compression de chemin
		return r;
	}

	// do union of classes of two elements i and j
	void union(int i, int j) {
		int ri = this.find(i);
		int rj = this.find(j);
		if (ri == rj) return; // already in the same class
		this.numClasses--;
		if (this.rank[ri] < this.rank[rj]){// rj become the representative
			this.link[ri] = rj;
			this.size[rj] = this.size[rj]+this.size[ri];
		}
		else {
			this.link[rj] = ri;// ri become the representative
			if (this.rank[ri] == this.rank[rj])
				this.rank[ri]++;
			this.size[ri] = this.size[rj]+this.size[ri];
		}
	}

	// returns the number of elements in the class of element i
	int getSize(int i){
		int ri = this.find(i);
		return this.size[ri];
	}

	// check if two elements i and j are in the same class
	boolean sameClass(int i, int j) {
		return this.find(i) == this.find(j);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.numClasses);
		for (int i = 0; i < this.link.length; i++)
			sb.append("[" + i + "->" + this.link[i] + "]");
		return sb.toString();
	}

}


class MazeFrame extends JFrame {

	MazeFrame(Cell[][] grid, int height, int width, int step) {
		MazeWindow window = new MazeWindow(grid, height, width, step);
		this.setTitle("labyrinthe");
		window.setPreferredSize(new Dimension(width * step + 1, height * step + 1));
		this.add(window, BorderLayout.CENTER);
		this.pack();
		this.add(window);
		this.addKeyListener(window);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

class MazeWindow extends JPanel implements KeyListener {

	private final int height, width, step;
	private Cell[][] grid;

	MazeWindow(Cell[][] grid, int height, int width, int step) {
		this.grid = grid;
		this.height = height;
		this.width = width;
		this.step = step;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		int w = this.width * this.step;
		int h = this.height * this.step;
		g.drawLine(0, 0, w, 0);
		g.drawLine(0, 0, 0, h);
		g.drawLine(w, 0, w, h);
		g.drawLine(0, h, w, h);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				// draw walls
				g2d.setColor(Color.BLACK);

				if(i > 0 && !grid[i][j].hasPassageTo(grid[i-1][j]))
					g2d.drawLine(j*step, i*step, (j+1)*step, i*step);
				if(j < width-1 && !grid[i][j].hasPassageTo(grid[i][j+1]))
					g2d.drawLine((j+1)*step, i*step, (j+1)*step, (i+1)*step);
				if(i < height-1 && !grid[i][j].hasPassageTo(grid[i+1][j]))
					g2d.drawLine(j*step, (i+1)*step, (j+1)*step, (i+1)*step);
				if(j > 0 && !grid[i][j].hasPassageTo(grid[i][j-1]))
					g2d.drawLine(j*step, i*step, j*step, (i+1)*step);

				// draw mark
				g2d.setColor(Color.RED);

				if(grid[i][j].isMarked()) {
					int midx = j*step + step/2;
					int midy = i*step + step/2;
					g2d.fillOval(midx - step/4, midy - step/4, step/2, step/2);
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent ev) {
		char key = ev.getKeyChar();
		if (key == 'q')
			System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent ev) {
	}

	@Override
	public void keyReleased(KeyEvent ev) {
	}

}

class Coordinate { // model a coordinate
	final int i,j;

	Coordinate(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public String toString() {
		return "(" + i + "," + j + ")";
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;

		if(!(o instanceof Coordinate))
			return false;

		Coordinate that = (Coordinate)o;

		return this.i == that.i && this.j == that.j;
	}

	@Override
	public int hashCode() {
		return 31*i + j;
	}
}

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * this class models a cell of the maze
 */
public abstract class Cell {

	private boolean isMarked; // if the cell is marked
	private boolean isExit; // if the cell is the exit of the maze
	/* list of neighboring cells of the cell */
	private ArrayList<Cell> neighbors; // 
	private ArrayList<Boolean> walls; // for each of the neighboring cells, if there is a wall in its direction
	protected Maze maze; // reference to the maze (for visualization)


	Cell() {}

	/**
	 * initialize a cell of the maze
	 */
	Cell(Maze maze) {
		isMarked = false;
		isExit = false;
		neighbors = new ArrayList<>();
		walls = new ArrayList<>();
		this.maze = maze;
	}

	/**
	 * allows to display the characteristics of a cell.
	 */
	public String toString(){
		String s= "Case ";
		if (!isMarked)
			s+="no ";
		s += "marked ";
		int n = getNeighbors(true).size();
		s += " with " + n + " neighbors whose ";
		n = getNeighbors(false).size();
		s += n + " accessible.";
		return s;
	}

	/**
	 * return the list of neighboring cells (accessible or not, according to the value of the argument)
	 *
	 * @return return only the list of cells to which a passage exists, if ignoreWalls==false. <br>
	 *         If ignoreWalls==true, then all neighboring cells are returned.
	 */
	List<Cell> getNeighbors(boolean ignoreWalls) {
		if(ignoreWalls)
			return new ArrayList<>(neighbors);
		else
			return neighbors.stream().filter(cell -> this.hasPassageTo(cell)).collect(Collectors.toList());
	}

	/**
	 * check if the cell 'c' is a neighbor and there is no wall in its direction
	 *
	 * @return true iff the cell 'c' is a neighbor and there is no wall in its direction
	 */
	boolean hasPassageTo(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			return false;

		return !this.walls.get(this.neighbors.indexOf(c));
	}

	/**
	 * delete the wall in the direction of the cell 'c' (if the current cell and the cell 'c' are adjacent)
	 */
	void breakWall(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			throw new IllegalArgumentException("cells are not neighbors");

		this.walls.set(this.neighbors.indexOf(c), false);
		c.walls.set(c.neighbors.indexOf(this), false);
	}

	/**
	 * check if the cell is marked
	 *
	 * @return true iff the cell is marked
	 */
	boolean isMarked() {
		return isMarked;
	}

	/**
	 * add a mark if b==true, remove the mark if b==false
	 */
	void setMarked(boolean b) {
		isMarked = b;
	}

	/**
	 * check if the cell is an exit
	 * @return true iff the cell is an exit
	 */
	boolean isExit() {
		return isExit;
	}

	/**
	 * declare the cell as an exit if b==true, <br> declare the cell as not an exit if b==false
	 */
	void setExit(boolean b) {
		isExit = b;
	}

	/**
	 * check if the cell is isolated (i.e. has walls in all directions)
	 *
	 * @return true iff the cell is isolated (i.e. has walls in all directions)
	 */
	boolean isIsolated() {
		return getNeighbors(false).isEmpty();
	}

	/**
	 * add a cell to the list of neighboring cells
	 */
	void addNeighbor(Cell n) {
		neighbors.add(n);
		walls.add(true);
	}

	/**
	 * check if there is a path from the current cell to an exit
	 *
	 * @return true iff there is a path from the current cell to an exit
	 */
	abstract boolean searchPath();

	/**
	 * generate a perfect maze using recursive backtracking
	 */
	abstract void generateRec();

}

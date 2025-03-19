/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

class Cell {
    int i, j;

    Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell c = (Cell) obj;
        return i == c.i && j == c.j;
    }
}

// apply both merge by rank & path compression
class UnionFind {
    private final int[][] rank; // the depth of the trees *before flatten*
    private final Cell[][] link; // the parent of cell

    UnionFind(int n) {
        rank = new int[n + 2][n + 2];
        link = new Cell[n + 2][n + 2];
        for (int i = 0; i < n + 2; i++)
            for (int j = 0; j < n + 2; j++)
                link[i][j] = new Cell(i, j);
    }

    Cell find(Cell c) {
        int i = c.i, j = c.j;
        if (!link[i][j].equals(c)) // not root
            link[i][j] = find(link[i][j]); // not modifying the rank, because of the definition of rank
        return link[i][j];
    }

    void union(Cell a, Cell b) {
        Cell r1 = find(a), r2 = find(b);
        if (r1.equals(r2)) // already connected
            return;
        // let bigger root be r1, smaller be r2
        if (rank[r1.i][r1.j] < rank[r2.i][r2.j]) { // if r2 is bigger than r1
            // swap two roots
            Cell temp = r1;
            r1 = r2;
            r2 = temp;
        }
        // merge two trees
        link[r2.i][r2.j] = r1;
        // increase rank if needed
        if (rank[r1.i][r1.j] == rank[r2.i][r2.j])
            rank[r1.i][r1.j]++;
    }

    boolean isConnected(Cell a, Cell b) {
        return find(a).equals(find(b));
    }
}

public class Hex {
    private final int size;
    private final Player[][] board;
    private Player currentPlayer;
    private final UnionFind unionFind;

    enum Player {
        NOONE, BLUE, RED
    }


    // create an empty board of size n*n
    Hex(int n) {
        size = n;
        board = new Player[n + 2][n + 2]; // add extra padding to simplify implement
        for (int i = 0; i < n + 2; i++)
            for (int j = 0; j < n + 2; j++)
                board[i][j] = Player.NOONE;
        for (int i = 1; i <= n; i++) {
            board[0][i] = Player.BLUE;
            board[n + 1][i] = Player.BLUE;
            board[i][0] = Player.RED;
            board[i][n + 1] = Player.RED;
        }
        currentPlayer = Player.BLUE;
        unionFind = new UnionFind(n);
        for (int i = 1; i < n; i++) {
            unionFind.union(new Cell(0, i), new Cell(0, i + 1));
            unionFind.union(new Cell(n + 1, i), new Cell(n + 1, i + 1));
            unionFind.union(new Cell(i, 0), new Cell(i + 1, 0));
            unionFind.union(new Cell(i, n + 1), new Cell(i + 1, n + 1));
        }
    }

    // return the color of cell i,j
    Player get(int i, int j) {
        return board[i][j];
    }

    // update the board after the player with the trait plays the cell (i, j).
    // Does nothing if the move is illegal.
    // Returns true if and only if the move is legal.
    boolean click(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) // out of board
            return false;
        if (board[i][j] != Player.NOONE) // cell already assigned
            return false;
        board[i][j] = currentPlayer;
        // if adjacent cell is same color as this block, union it
        final int[][] directions = {
                {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}
        };
        for (int[] direction : directions) {
            int x = i + direction[0];
            int y = j + direction[1];
            if (board[x][y].equals(board[i][j]))
                unionFind.union(new Cell(x, y), new Cell(i, j));
        }
        currentPlayer = (currentPlayer == Player.BLUE ? Player.RED : Player.BLUE); // switch player
        return true;
    }

    // return the player with the trait or Player.NOONE if the game is over
    // because of a player's victory.
    Player currentPlayer() {
        Player winner = this.winner();
        if (winner == Player.NOONE)
            return currentPlayer;
        return winner;
    }

    // return the winning player, or Player.NOONE if no player has won yet
    Player winner() {
        if (unionFind.isConnected(new Cell(0, 1), new Cell(size + 1, size)))
            return Player.BLUE;
        if (unionFind.isConnected(new Cell(1, 0), new Cell(size, size + 1)))
            return Player.RED;
        return Player.NOONE;
    }

    int label(int i, int j) {
        return i * 100 + j; // first 2 digit for i, last 2 for j
    }

    public static void main(String[] args) {
        HexGUI.createAndShowGUI();
    }
}

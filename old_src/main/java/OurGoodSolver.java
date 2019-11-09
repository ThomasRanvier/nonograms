import java.util.LinkedList;

public class OurGoodSolver {
    static int height, width;
    static int[][] colVal, colIx, rows, cols;
    static long[] grid, mask, val;
    static long[][] rowPerms;

    public static boolean solve(int[][][] constraints, int w, int h) {
        height = h;
        width = w;
        rows = constraints[0];
        cols = constraints[1];
        grid = new long[height];
        rowPerms = new long[height][];

        for (int r = 0; r < height; r++) {
            LinkedList<Long> res = new LinkedList<>();
            int spaces = width - (rows[r].length - 1);
            for (int i = 0; i < rows[r].length; i++)
                spaces -= rows[r][i];
            calcPerms(r, 0, spaces, 0, 0, res);
            if (res.isEmpty())
                return false;
            rowPerms[r] = new long[res.size()];
            while (!res.isEmpty())
                rowPerms[r][res.size() - 1] = res.pollLast();
        }

        colVal = new int[height][width];
        colIx = new int[height][width];
        mask = new long[height];
        val = new long[height];
        if (dfs(0)) {
            //display();
            return true;
        } else
            return false;
    }

    private static void display() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++)
                System.out.print((grid[r] & (1L << c)) == 0 ? "  " : "##");
            System.out.println();
        }
    }

    private static boolean dfs(int row){
        if (row == height)
            return true;
        rowMask(row);
        for (int i = 0; i < rowPerms[row].length; i++){
            if ((rowPerms[row][i] & mask[row]) != val[row])
                continue;
            grid[row] = rowPerms[row][i];
            updateCols(row);
            if (dfs(row + 1))
                return true;
        }
        return false;
    }

    private static void rowMask(int row){
        mask[row] = val[row] = 0;
        if (row == 0)
            return;
        long ixc = 1L;
        for (int c = 0; c < width; c++, ixc <<= 1) {
            if (colVal[row-1][c] > 0) {
                mask[row] |= ixc;
                if (cols[c][colIx[row-1][c]] > colVal[row-1][c])
                    val[row] |= ixc;
            } else if(colVal[row-1][c] == 0 && colIx[row-1][c] == cols[c].length)
                mask[row] |= ixc;
        }
    }

    private static void updateCols(int row){
        long ixc = 1L;
        for (int c = 0; c < width; c++, ixc <<= 1) {
            colVal[row][c] = row == 0 ? 0 : colVal[row-1][c];
            colIx[row][c] = row == 0 ? 0 : colIx[row-1][c];
            if ((grid[row] & ixc) == 0) {
                if (row > 0 && colVal[row-1][c] > 0) {
                    colVal[row][c]=0;
                    colIx[row][c]++;
                }
            } else
                colVal[row][c]++;
        }
    }

    private static void calcPerms(int r, int cur, int spaces, long perm, int shift, LinkedList<Long> res){
        if (cur == rows[r].length) {
            if ((grid[r] & perm) == grid[r])
                res.add(perm);
            return;
        }
        while (spaces >= 0) {
            calcPerms(r, cur + 1, spaces, perm | (toBinary(rows[r][cur]) << shift),
                    shift + rows[r][cur] + 1, res);
            shift++;
            spaces--;
        }
    }

    private static long toBinary(int b) {
        return (1L << b) - 1;
    }
}

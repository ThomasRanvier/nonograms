import java.util.ArrayList;
import java.util.Random;

public class Utils {
    public static int[][][] generateNonogram(int width, int height, double rate, int seed) {
        boolean[][] nonogram = new boolean[width][height];
        Random rand = seed <= 0 ? new Random() : new Random(seed);
        int[][] linesConstraints = new int[height][];
        int[][] columnsConstraints = new int[width][];
        ArrayList<Integer>[] columnConstraints = new ArrayList[width];
        for (int x = 0; x < width; x++) {
            columnConstraints[x] = new ArrayList<Integer>();
        }
        int[] c = new int[width];
        for (int y = 0; y < height; y++) {
            String line = "";
            ArrayList<Integer> lineConstraints = new ArrayList<Integer>();
            int l = 0;
            for (int x = 0; x < width; x++) {
                nonogram[x][y] = rand.nextInt(100) < (rate * 100);
                line += nonogram[x][y] ? "##" : "  ";
                if (nonogram[x][y]) {
                    l++;
                    c[x]++;
                    if (x == width - 1) {
                        lineConstraints.add(l);
                    }
                    if (y == height - 1) {
                        columnConstraints[x].add(c[x]);
                    }
                } else {
                    if (l > 0) {
                        lineConstraints.add(l);
                    }
                    if (c[x] > 0) {
                        columnConstraints[x].add(c[x]);
                    }
                    l = 0;
                    c[x] = 0;
                }
            }
            int[] lineC = new int[lineConstraints.size()];
            for (int i = 0; i < lineConstraints.size(); i++) {
                lineC[i] = lineConstraints.get(i);
            }
            linesConstraints[y] = lineC;

            for (int x = 0; x < width; x++) {
                int[] columnC = new int[columnConstraints[x].size()];
                for (int i = 0; i < columnConstraints[x].size(); i++) {
                    columnC[i] = columnConstraints[x].get(i);
                }
                columnsConstraints[x] = columnC;
            }
            System.out.println(line);
        }
        int[][][] constraints = new int[][][]{linesConstraints, columnsConstraints};
        /*
        for (int y = 0; y < height; y++) {
            String line = "";
            for (int i = 0; i < constraints[0][y].length; i++) {
                line += constraints[0][y][i] + " ";
            }
            System.out.println(line);
        }
        //System.out.println();
        for (int x = 0; x < width; x++) {
            String line = "";
            for (int i = 0; i < constraints[1][x].length; i++) {
                line += constraints[1][x][i] + " ";
            }
            System.out.println(line);
        }
         */
        return constraints;
    }
}

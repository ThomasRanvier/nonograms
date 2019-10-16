import java.util.ArrayList;
import java.util.Arrays;

public class BrutForceSolver {
    public static boolean brutForceMethod(int width, int height, int[][][] constraints) {
        ArrayList<int[]>[] cols, rows;
        rows = getCandidates(constraints[0], height);
        cols = getCandidates(constraints[1], width);
        return true;
    }

    public static ArrayList<int[]>[] getCandidates(int[][] constraints, int size) {
        int constraintsCount = constraints.length;
        ArrayList<int[]>[] allCandidates = new ArrayList[constraintsCount];
        for (int i = 0; i < constraintsCount; i++) {
            ArrayList<int[]> lineCandidates = new ArrayList<int[]>();
            int[] cstr = constraints[i];
            if (cstr.length == 0) {
                lineCandidates.add(new int[]{size});
            } else {
                String line = "";
                for (int ci : cstr) {
                    line += ci + " ";
                }
                System.out.println("");
                System.out.println("cstr : " + line);

                int spacesToAdd = size;
                int nPossiblePlaces = 2;
                int[] neededCombinations = new int[(cstr.length * 2) + 1];
                for (int c = 0; c < cstr.length; c++) {
                    neededCombinations[(2 * c) + 1] = cstr[c];
                    spacesToAdd -= cstr[c];
                    if (c < cstr.length - 1) {
                        neededCombinations[(c * 2) + 2] = 1;
                        spacesToAdd--;
                        nPossiblePlaces++;
                    }
                }

                line = "";
                for (int ci : neededCombinations) {
                    line += ci + " ";
                }
                System.out.println("needed : " + line);
                System.out.println("spaces : " + spacesToAdd);
                System.out.println("possible places : " + nPossiblePlaces);

                if (spacesToAdd == 0) {
                    lineCandidates.add(neededCombinations);
                } else {
                    ArrayList<int[]> spacePlacements = new ArrayList<int[]>();
                    combinationsWithReplacement(spacesToAdd, nPossiblePlaces, lineCandidates);
                    //generateCandidates(neededCombinations, spacePlacements, lineCandidates);
                }
                System.out.println("line candidates : ");
                for (int[] a : lineCandidates) {
                    line = "";
                    for (int x : a) {
                        line += x + " ";
                    }
                    System.out.println(line);
                }
            }
            allCandidates[i] = lineCandidates;
        }
        return allCandidates;
    }

    private static void generateCandidates(int[] neededCombinations, ArrayList<int[]> spacePlacements, ArrayList<int[]> lineCandidates) {
        for (int[] sp : spacePlacements) {
            int[] cand = neededCombinations.clone();
            for (int s : sp) {
                cand[s * 2]++;
            }
            lineCandidates.add(cand.clone());
        }
    }

    private static void combinationsWithReplacement(int n, int r, ArrayList<int[]> list) {
        int[] indices = new int[n];
        list.add(indices.clone());
        for (int _ = 0 ; _ < r - 1; _++) {
            for (int i = n - 1; i >= 0; i--) {
                indices[i]++;
                for (int ii = n - 1; ii > i; ii--) {
                    indices[ii] = indices[i];
                }
                list.add(indices.clone());
                if (i > 0) {
                    for (int ii = n - 1; ii >= i; ii--) {
                        while(indices[ii] < r - 1) {
                            indices[ii]++;
                            list.add(indices.clone());
                        }
                    }
                }
            }
        }
    }
}

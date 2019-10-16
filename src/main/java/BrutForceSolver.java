import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BrutForceSolver {
    public static boolean brutForceMethod(int width, int height, int[][][] constraints) {
        List<List<BitSet>> cols, rows;
        rows = getCandidates(constraints[0], height);
        cols = getCandidates(constraints[1], width);
        int numChanged;
        do {
            numChanged = reduceMutual(cols, rows);
            if (numChanged == -1) {
                return false;
            }
        } while (numChanged > 0);

        /*
        System.out.println();
        System.out.println();
        for (List<BitSet> row : rows) {
            for (int i = 0; i < cols.size(); i++)
                System.out.print(row.get(0).get(i) ? "##" : "  ");
            System.out.println();
        }
        System.out.println();
        System.out.println();
         */

        return true;
    }

    private static int reduceMutual(List<List<BitSet>> rows, List<List<BitSet>> cols) {
        int countRemoved1 = reduce(cols, rows);
        if (countRemoved1 == -1)
            return -1;

        int countRemoved2 = reduce(rows, cols);
        if (countRemoved2 == -1)
            return -1;

        return countRemoved1 + countRemoved2;
    }

    private static int reduce(List<List<BitSet>> a, List<List<BitSet>> b) {
        int countRemoved = 0;
        for (int i = 0; i < a.size(); i++) {
            BitSet commonOn = new BitSet();
            commonOn.set(0, b.size());
            BitSet commonOff = new BitSet();
            for (BitSet candidate : a.get(i)) {
                commonOn.and(candidate);
                commonOff.or(candidate);
            }
            for (int j = 0; j < b.size(); j++) {
                final int fi = i, fj = j;
                if (b.get(j).removeIf(cnd -> (commonOn.get(fj) && !cnd.get(fi)) || (!commonOff.get(fj) && cnd.get(fi))))
                    countRemoved++;
                if (b.get(j).isEmpty())
                    return -1;
            }
        }
        return countRemoved;
    }

    public static List<List<BitSet>> getCandidates(int[][] constraints, int size) {
        /*
        System.out.println("");
        System.out.println("");
        System.out.println("______________");
        System.out.println("______________");
        System.out.println("Get candidates");
        System.out.println("______________");
        System.out.println("______________");
        System.out.println("");
        System.out.println("");

         */

        int constraintsCount = constraints.length;
        List<List<BitSet>> allCandidates = new ArrayList<>();
        for (int i = 0; i < constraintsCount; i++) {
            ArrayList<int[]> lineCandidates = new ArrayList<int[]>();
            int[] cstr = constraints[i];
            if (cstr.length == 0) {
                lineCandidates.add(new int[]{size});
            } else {
                /*
                String line = "";
                for (int ci : cstr) {
                    line += ci + " ";
                }
                System.out.println("");
                System.out.println("cstr : " + line);

                 */

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

                /*
                line = "";
                for (int ci : neededCombinations) {
                    line += ci + " ";
                }
                System.out.println("needed : " + line);
                System.out.println("spaces : " + spacesToAdd);
                System.out.println("possible places : " + nPossiblePlaces);

                 */

                if (spacesToAdd == 0) {
                    lineCandidates.add(neededCombinations);
                } else {
                    ArrayList<int[]> spacePlacements = new ArrayList<int[]>();
                    combinationsWithReplacement(spacesToAdd, nPossiblePlaces, spacePlacements);
                    generateCandidates(neededCombinations, spacePlacements, lineCandidates);
                }

                /*
                System.out.println("line candidates : ");
                for (int[] a : lineCandidates) {
                    line = "";
                    int index = 0;
                    for (int x : a) {
                        for(int _ = 0; _ < x; _++) {
                            line += (index % 2) == 0 ? "  " : "##";
                        }
                        index++;
                    }
                    System.out.println(line);
                }

                 */
            }

            List<BitSet> lineCandidatesBitSet = new ArrayList<>();
            for (int[] lc: lineCandidates) {
                BitSet lcBitSet = new BitSet(size);
                int index = 0;
                int j = 0;
                for (int e : lc) {
                    for (int _ = 0; _ < e; _++) {
                        lcBitSet.set(j, !((index % 2) == 0));
                        j++;
                    }
                    index++;
                }
                lineCandidatesBitSet.add(lcBitSet);
            }
            allCandidates.add(lineCandidatesBitSet);
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

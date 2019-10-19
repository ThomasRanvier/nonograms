import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class OurSolver {
    public static boolean ourMethod(String method, int width, int height, int[][][] constraints) {
        List<List<BitSet>> cols, rows;
        cols = getCandidates(constraints[1], width);
        rows = null;
        boolean result = false;
        switch (method) {
            case "brutforce":
                result = brutforceMethod(cols, width, height, constraints[0]);
                break;
            case "reductive":
                rows = getCandidates(constraints[0], height);
                result = reductiveMethod(cols, rows);
                break;
        }

        if (result && method == "reductive") {
            System.out.println();
            System.out.println();
            for (List<BitSet> row : rows) {
                for (int i = 0; i < cols.size(); i++)
                    System.out.print(row.get(0).get(i) ? "##" : "  ");
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }

        return result;
    }

    private static boolean brutforceMethod(List<List<BitSet>> cols, int width, int height, int[][] rowsConstraints) {
        int testedPossibilities = 0;
        List<BitSet[]> combinations = combine(cols);

        for (BitSet[] comb : combinations) {
            testedPossibilities++;
            if (solved(comb, rowsConstraints, height)) {
                for (int rowIndex = 0; rowIndex < height; rowIndex++) {
                    for (BitSet c : comb) {
                        System.out.print(c.get(rowIndex) ? "##" : "  ");
                    }
                    System.out.println("");
                }
                System.out.println("Possibilités parcourues : " + testedPossibilities);
                return true;
            }
        }
        System.out.println("Possibilités parcourues : " + testedPossibilities);
        return false;
    }

    public static boolean solved(BitSet[] puzzle, int[][] rowsConstraints, int height) {
        //Need to check rows constraints
        //We go through the lines of puzzle
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            /*
            System.out.println("constraints : ");
            for (int c : rowsConstraints[rowIndex]) {
                System.out.print(c);
            }
            System.out.println("");
             */
            int constraintIndex = 0;
            int adjacentTrue = 0;
            for (BitSet col : puzzle) {
                boolean currentValue = col.get(rowIndex);
                //System.out.println("constraint : " + constraintIndex);
                //System.out.println("currentValue : " + currentValue);
                if (currentValue) {
                    adjacentTrue++;
                } else {
                    if (adjacentTrue > 0) {
                        if (constraintIndex >= rowsConstraints[rowIndex].length) {
                            return false;
                        }
                        if (rowsConstraints[rowIndex][constraintIndex] != adjacentTrue) {
                            return false;
                        }
                        constraintIndex++;
                    }
                    adjacentTrue = 0;
                }
            }
            if (constraintIndex == 0 && rowsConstraints[rowIndex].length > 1) {
                return false;
            }
            //System.out.println("constraint : " + constraintIndex);
            //System.out.println("adjacentTrue : " + adjacentTrue);
            if (adjacentTrue > 0) {
                if (constraintIndex >= rowsConstraints[rowIndex].length) {
                    return false;
                }
                if (rowsConstraints[rowIndex][constraintIndex] != adjacentTrue) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean reductiveMethod(List<List<BitSet>> cols, List<List<BitSet>> rows) {
        int numChanged;
        do {
            numChanged = reduceMutual(cols, rows);
            if (numChanged == -1) {
                return false;
            }
        } while (numChanged > 0);
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

    public static List<BitSet[]> combine(final List<List<BitSet>> containers) {
        List<List<BitSet>> c = combineInternal(0, containers);
        List<BitSet[]> r = new ArrayList<>();
        for (List<BitSet> p : c) {
            BitSet[] temp = new BitSet[p.size()];
            for (int i = 0; i < p.size(); i++) {
                temp[i] = p.get(i);
            }
            r.add(temp);
        }
        return r;
    }

    private static List<List<BitSet>> combineInternal(final int currentIndex, final List<List<BitSet>> containers) {
        if (currentIndex == containers.size()) {
            // Skip the items for the last container
            final List<List<BitSet>> combinations = new ArrayList<>();
            combinations.add(Collections.emptyList());
            return combinations;
        }

        final List<List<BitSet>> combinations = new ArrayList<>();
        List<BitSet> containerItemList = containers.get(currentIndex);
        // Get combination from next index
        final List<List<BitSet>> suffixList = combineInternal(currentIndex + 1, containers);

        final int size = containerItemList.size();
        for (int i = 0; i < size; i++) {
            final BitSet containerItem = containerItemList.get(i);
            if (suffixList != null) {
                for (final List<BitSet> suffix : suffixList) {
                    final List<BitSet> nextCombination = new ArrayList<>();
                    nextCombination.add(containerItem);
                    nextCombination.addAll(suffix);
                    combinations.add(nextCombination);
                }
            }
        }

        return combinations;
    }
}

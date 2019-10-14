/*
 * Copyright (C) 2017 COSLING S.A.S.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton;
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.util.tools.ArrayUtils;

public class Main {
    public static void main(String[] args) {
        // number of columns
        int N = 21;
        // number of rows
        int M = 21;
        // sequence of shaded blocks
        int[][][] BLOCKS =
                new int[][][]{{
                        {7, 2, 2, 7},
                        {1, 1, 1, 2, 1, 1},
                        {1, 3, 1, 3, 1, 1, 3, 1},
                        {1, 3, 1, 2, 1, 1, 3, 1},
                        {1, 3, 1, 2, 1, 3, 1},
                        {1, 1, 2, 2, 1, 1},
                        {7, 1, 1, 1, 7},
                        {2},
                        {2, 3, 2, 1, 4},
                        {1, 1, 3, 3, 2, 1},
                        {3, 1, 3, 2, 2},
                        {1, 1, 1, 3, 1, 1},
                        {1, 5, 1, 1, 1, 1},
                        {1, 1, 1, 1, 3, 1},
                        {7, 1, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 3, 1, 1, 1, 2, 2},
                        {1, 3, 1, 2, 1, 2, 1, 1},
                        {1, 3, 1, 1, 1, 2},
                        {1, 1, 2, 1, 1},
                        {7, 1, 3, 1},
                }, {
                        {7, 1, 2, 7},
                        {1, 1, 1, 1, 1, 1},
                        {1, 3, 1, 1, 1, 3, 1},
                        {1, 3, 1, 1, 1, 1, 3, 1},
                        {1, 3, 1, 1, 1, 1, 3, 1},
                        {1, 1, 2, 1, 1},
                        {7, 1, 1, 1, 7},
                        {4},
                        {4, 2, 2, 2, 2, 2},
                        {1, 2, 1, 1, 1, 2, 3},
                        {1, 2, 2, 2},
                        {2, 3, 1, 1, 1, 1, 1},
                        {3, 3, 2, 3, 1, 1},
                        {1, 1, 3, 2},
                        {7, 1, 1},
                        {1, 1, 1, 1, 1, 1, 1},
                        {1, 3, 1, 3, 2, 3},
                        {1, 3, 1, 2, 2, 1, 1},
                        {1, 3, 1, 1, 1, 1, 1},
                        {1, 1, 5, 3},
                        {7, 1, 1, 2, 1},
                }};

        Model model = new Model("Nonogram");
        // Variables declaration
        BoolVar[][] cells = model.boolVarMatrix("c", N, M);
        // Constraint declaration
        // one regular per row
        for (int i = 0; i < M; i++) {
            dfa2(cells[i], BLOCKS[0][i], model);
        }
        for (int j = 0; j < N; j++) {
            dfa2(ArrayUtils.getColumn(cells, j), BLOCKS[1][j], model);
        }
        if(model.getSolver().solve()){
            for (int i = 0; i < cells.length; i++) {
                System.out.printf("\t");
                for (int j = 0; j < cells[i].length; j++) {
                    System.out.printf(cells[i][j].getValue() == 1 ? "##" : "  ");
                }
                System.out.printf("\n");
            }
        }
    }

    private static void dfa(BoolVar[] cells, int[] rest, Model model) {
        StringBuilder regexp = new StringBuilder("0*");
        int m = rest.length;
        for (int i = 0; i < m; i++) {
            regexp.append('1').append('{').append(rest[i]).append('}');
            regexp.append('0');
            regexp.append(i == m - 1 ? '*' : '+');
        }
        IAutomaton auto = new FiniteAutomaton(regexp.toString());
        model.regular(cells, auto).post();
    }

    private static void dfa2(BoolVar[] cells, int[] seq, Model model) {
        FiniteAutomaton auto = new FiniteAutomaton();
        int si = auto.addState();
        auto.setInitialState(si); // declare it as initial state
        int i0 = auto.addState();
        auto.addTransition(si, i0, 0); // first transition
        auto.addTransition(i0, i0, 0); // second transition
        int from = i0;
        int m = seq.length;
        for (int i = 0; i < m; i++) {
            int ii = auto.addState();
            // word can start with '1'
            if(i == 0){
                auto.addTransition(si, ii, 1);
            }
            auto.addTransition(from, ii, 1);
            from = ii;
            for(int j = 1; j < seq[i]; j++){
                int jj = auto.addState();
                auto.addTransition(from, jj, 1);
                from = jj;
            }
            int ii0 = auto.addState();
            auto.addTransition(from, ii0, 0);
            auto.addTransition(ii0, ii0, 0);
            // the word can end with '1' or '0'
            if(i == m - 1){
                auto.setFinal(from, ii0);
            }
            from = ii0;
        }
        model.regular(cells, auto).post();
    }
}

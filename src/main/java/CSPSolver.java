import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton;
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.util.tools.ArrayUtils;

public class CSPSolver {
    private static void regexpConstraints(BoolVar[] cells, int[] rest, Model model) {
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

    private static void constructiveConstraints(BoolVar[] cells, int[] seq, Model model) {
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

    public static boolean cspMethod(String method, int width, int height, int[][][] constraints) {
        Model model = new Model("Nonogram");
        BoolVar[][] cells = model.boolVarMatrix("c", height, width);

        for (int i = 0; i < height; i++) {
            if (method.equals("regexp")) {
                regexpConstraints(cells[i], constraints[0][i], model);
            } else {
                constructiveConstraints(cells[i], constraints[0][i], model);
            }
        }
        for (int j = 0; j < width; j++) {
            if (method.equals("regexp")) {
                regexpConstraints(ArrayUtils.getColumn(cells, j), constraints[1][j], model);
            } else {
                constructiveConstraints(ArrayUtils.getColumn(cells, j), constraints[1][j], model);
            }
        }
        if(model.getSolver().solve()){
            displayResult(cells);
            return true;
        }
        return false;
    }

    private static void displayResult(BoolVar[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            System.out.printf("\t");
            for (int j = 0; j < cells[i].length; j++) {
                System.out.printf(cells[i][j].getValue() == 1 ? "##" : "  ");
            }
            System.out.printf("\n");
        }
    }
}

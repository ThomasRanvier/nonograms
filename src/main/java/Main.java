
public class Main {
    public static void main(String[] args) {
        int width = 8;
        int height = 8;
        int[][][] constraints = Utils.generateNonogram(width, height, 0.5, 0);

        /*
        CSPSolver.cspMethod("regexp", width, height, constraints);

        long startTime = System.nanoTime();
        if (CSPSolver.cspMethod("regexp", width, height, constraints)) {
            long endTime = System.nanoTime();
            System.out.println("Regexp method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
        } else {
            System.out.println("Regexp method, failed");
        }
        */
        CSPSolver.cspMethod("constructive", width, height, constraints);
        long startTime = System.nanoTime();
        if (CSPSolver.cspMethod("constructive", width, height, constraints)) {
            long endTime = System.nanoTime();
            System.out.println("Constructive method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
        } else {
            System.out.println("Constructive method, failed");
        }

        startTime = System.nanoTime();
        if (BrutForceSolver.brutForceMethod(width, height, constraints)) {
            long endTime = System.nanoTime();
            System.out.println("Brut force, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
        } else {
            System.out.println("Brut force, failed");
        }
    }
}

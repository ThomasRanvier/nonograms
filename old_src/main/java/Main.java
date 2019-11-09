import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int width = 17;
        int height = 17;
        //BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true));

        for (int i = 0; i < 1; i++) {
            int[][][] constraints = Utils.generateNonogram(width, height, 0.4, 0);
            System.out.println("");
            System.out.println("");
            System.out.println("CSP solver, constructive method start");
            long startTime = System.nanoTime();
            if (CSPSolver.solve(width, height, constraints)) {
                long endTime = System.nanoTime();
                System.out.println("CSP solver, constructive method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
                //writer.write(((endTime - startTime) / 1000000000.0) + "\n");
            } else {
                System.out.println("CSP solver, constructive method, failed");
            }
            System.out.println("");
            System.out.println("");
            System.out.println("Our good method start");
            startTime = System.nanoTime();
            if (OurGoodSolver.solve(constraints, width, height)) {
                long endTime = System.nanoTime();
                System.out.println("Our good method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
                //writer.write(((endTime - startTime) / 1000000000.0) + "\n");
            } else {
                System.out.println("Our good method, failed");
            }
        }
        //writer.close();
    }
}

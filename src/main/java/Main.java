
public class Main {
    public static void main(String[] args) {
        int width = 6;
        int height = 6;
        int[][][] constraints = Utils.generateNonogram(width, height, 0.5, 0);

        long startTime = System.nanoTime();
        System.out.println("");
        System.out.println("");
        System.out.println("Constructive method start");
        if (CSPSolver.cspMethod("constructive", width, height, constraints)) {
            long endTime = System.nanoTime();
            System.out.println("Constructive method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
        } else {
            System.out.println("Constructive method, failed");
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Brut force method start");
        startTime = System.nanoTime();
        if (OurSolver.ourMethod("brutforce", width, height, constraints)) {
            long endTime = System.nanoTime();
            System.out.println("Brut force, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
        } else {
            System.out.println("Brut force, failed");
        }
    }
}

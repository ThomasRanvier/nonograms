import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int width = 40;
        int height = 40;
        BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true));

        for (int i = 0; i < 1; i++) {
            int[][][] constraints = Utils.generateNonogram(width, height, 0.8, 0);

            long startTime = System.nanoTime();
            System.out.println("");
            System.out.println("");
            System.out.println("Constructive method start");
            if (CSPSolver.cspMethod("constructive", width, height, constraints)) {
                long endTime = System.nanoTime();
                System.out.println("Constructive method, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
                String s = "\n" + ((endTime - startTime) / 1000000000.0);
                writer.append(s);
            } else {
                System.out.println("Constructive method, failed");
            }
/*
            System.out.println("");
            System.out.println("");
            System.out.println("Reductive method start");
            startTime = System.nanoTime();
            if (OurSolver.ourMethod("reductive", width, height, constraints)) {
                long endTime = System.nanoTime();
                System.out.println("Reductive, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
                writer.write(((endTime - startTime) / 1000000000.0) + "\n");
            } else {
                System.out.println("Reductive, failed");
            }

            System.out.println("");
            System.out.println("");
            System.out.println("Brut force method start");
            startTime = System.nanoTime();
            if (OurSolver.ourMethod("brutforce", width, height, constraints)) {
                long endTime = System.nanoTime();
                System.out.println("Brut force, solved in " + ((endTime - startTime) / 1000000000.0)  + " sec.");
                writer.write(((endTime - startTime) / 1000000000.0) + "\n");
            } else {
                System.out.println("Brut force, failed");
            }*/
        }
        writer.close();
    }
}

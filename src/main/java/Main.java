public class Main {
    public static void main(String[] args) {
        int width = 30;
        int height = 15;
        int[][][] constraints = Utils.generateNonogram(width, height, 0.5, 0);
        CSPSolver.cspMethod("constructive", width, height, constraints);
    }
}

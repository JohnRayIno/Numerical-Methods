import java.util.ArrayList;
import java.util.List;

public class GaussSeidelMethod {

    public static class IteRslt {
        public int iteration;
        public double[] x;
        public double ea;

        public IteRslt(int iteration, double[] x, double ea) {
            this.iteration = iteration;
            this.x = x;
            this.ea = ea;
        }
    }

    public static class GaussSeidelRslt {
        public List<IteRslt> iterations;
        public double[] solution;
        public double[][] A;
        public double[] b;
        public double[] initialGuess;

        public GaussSeidelRslt(List<IteRslt> iterations, double[] solution, double[][] A, double[] b, double[] initialGuess) {
            this.iterations = iterations;
            this.solution = solution;
            this.A = A;
            this.b = b;
            this.initialGuess = initialGuess;
        }
    }

    public static GaussSeidelRslt solve(double[][] A, double[] b, double[] initialGuess, double tolerance, int maxIterations, int decimals) {
        int n = A.length;
        List<IteRslt> iterations = new ArrayList<>();
        double[] x = initialGuess.clone();
        double ea = Double.MAX_VALUE;

        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            double[] xOld = x.clone();
            double maxEa = 0;

            // Gauss-Seidel iteration
            for (int i = 0; i < n; i++) {
                double sum1 = 0, sum2 = 0;
                for (int j = 0; j < i; j++) {
                    sum1 += A[i][j] * x[j];
                }
                for (int j = i + 1; j < n; j++) {
                    sum2 += A[i][j] * xOld[j];
                }
                if (A[i][i] == 0) {
                    throw new ArithmeticException("Zero diagonal element in Gauss-Seidel");
                }
                x[i] = round((b[i] - sum1 - sum2) / A[i][i], decimals);
                double currEa = Math.abs((x[i] - xOld[i]) / x[i]);
                maxEa = Math.max(maxEa, currEa);
            }

            ea = round(maxEa, decimals);
            iterations.add(new IteRslt(iteration, x.clone(), ea));

            if (ea <= tolerance) {
                break;
            }

            if (iteration == maxIterations) {
                throw new ArithmeticException("Gauss-Seidel did not converge within max iterations");
            }
        }

        return new GaussSeidelRslt(iterations, x, A, b, initialGuess);
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
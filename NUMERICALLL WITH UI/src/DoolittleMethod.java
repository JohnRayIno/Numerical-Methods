import java.util.ArrayList;
import java.util.List;

public class DoolittleMethod {

    public static class DoolittleRslt {
        public List<double[]> iterations; // Store solution vector x at each step
        public double[] solution;
        public double[][] L;
        public double[][] U;
        public double[][] A;
        public double[] b;

        public DoolittleRslt(List<double[]> iterations, double[] solution, double[][] L, double[][] U, double[][] A, double[] b) {
            this.iterations = iterations;
            this.solution = solution;
            this.L = L;
            this.U = U;
            this.A = A;
            this.b = b;
        }
    }

    public static DoolittleRslt solve(double[][] A, double[] b, int decimals) {
        int n = A.length;
        List<double[]> iterations = new ArrayList<>();
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L and U
        for (int i = 0; i < n; i++) {
            L[i][i] = 1; // Diagonal of L is 1
            for (int j = 0; j < n; j++) {
                if (i != j) L[i][j] = 0;
                U[i][j] = 0;
            }
        }

        // LU Decomposition
        for (int k = 0; k < n; k++) {
            // Compute U's kth row
            for (int j = k; j < n; j++) {
                double sum = 0;
                for (int m = 0; m < k; m++) {
                    sum += L[k][m] * U[m][j];
                }
                U[k][j] = round(A[k][j] - sum, decimals);
            }

            // Compute L's kth column
            for (int i = k + 1; i < n; i++) {
                double sum = 0;
                for (int m = 0; m < k; m++) {
                    sum += L[i][m] * U[m][k];
                }
                if (U[k][k] == 0) {
                    throw new ArithmeticException("Zero pivot in Doolittle decomposition");
                }
                L[i][k] = round((A[i][k] - sum) / U[k][k], decimals);
            }
        }

        // Forward substitution: Ly = b
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = round(b[i] - sum, decimals);
        }
        iterations.add(y.clone()); // Store intermediate y

        // Backward substitution: Ux = y
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            if (U[i][i] == 0) {
                throw new ArithmeticException("Zero pivot in backward substitution");
            }
            x[i] = round((y[i] - sum) / U[i][i], decimals);
        }
        iterations.add(x.clone()); // Store final solution

        return new DoolittleRslt(iterations, x, L, U, A, b);
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.util.ArrayList;
import java.util.List;

public class SimpsonsRule {

    public static class SimpsonsRslt {
        public double integral;
        public double a, b;
        public int n;
        public String function;
        public List<Double> points; // x values evaluated
        public List<Double> values; // f(x) values

        public SimpsonsRslt(double integral, double a, double b, int n, String function, List<Double> points, List<Double> values) {
            this.integral = integral;
            this.a = a;
            this.b = b;
            this.n = n;
            this.function = function;
            this.points = points;
            this.values = values;
        }
    }

    public static SimpsonsRslt solve(String functionStr, double a, double b, int n, int decimals) {
        if (n % 2 != 0) {
            throw new IllegalArgumentException("Number of segments must be even for Simpson's 1/3 Rule");
        }

        Expression expr = new ExpressionBuilder(functionStr)
                .variables("x")
                .functions(
                        new Function("sin", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.sin(args[0]);
                            }
                        },
                        new Function("cos", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.cos(args[0]);
                            }
                        },
                        new Function("tan", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.tan(args[0]);
                            }
                        }
                )
                .build();

        double h = (b - a) / n;
        List<Double> points = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        double sum = eval(expr, a) + eval(expr, b);

        points.add(a);
        values.add(eval(expr, a));

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            points.add(x);
            double fx = eval(expr, x);
            values.add(fx);
            sum += (i % 2 == 0) ? 2 * fx : 4 * fx;
        }

        points.add(b);
        values.add(eval(expr, b));

        double integral = round((h / 3) * sum, decimals);

        return new SimpsonsRslt(integral, a, b, n, functionStr, points, values);
    }

    private static double eval(Expression expr, double x) {
        return expr.setVariable("x", x).evaluate();
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
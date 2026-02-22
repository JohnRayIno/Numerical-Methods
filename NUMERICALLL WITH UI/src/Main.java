import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private JTextField functionInput;
    private JTextArea outputArea;

    public Main() {
        setTitle("Numerical Methods GUI");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 600));

        // Function input
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel functionLabel = new JLabel("Enter Function f(x):");
        inputPanel.add(functionLabel, BorderLayout.NORTH);

        functionInput = new JTextField();
        functionInput.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(functionInput, BorderLayout.CENTER);

        // Method buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] methods = {"Bisection", "Secant", "Doolittle", "Gauss-Seidel", "Simpson's 1/3"};
        for (String method : methods) {
            JButton button = new JButton(method);
            button.addActionListener(e -> onClicked(method));
            buttonsPanel.add(button);
        }

        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Final layout
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void onClicked(String method) {
        String function = functionInput.getText();
        outputArea.setText("");

        try {
            switch (method) {
                case "Bisection":
                    if (function.isEmpty()) {
                        outputArea.setText("Please enter a function.");
                        return;
                    }
                    double lower = Double.parseDouble(JOptionPane.showInputDialog("Enter lower bound (x0):"));
                    double upper = Double.parseDouble(JOptionPane.showInputDialog("Enter upper bound (x1):"));
                    double tolB = Double.parseDouble(JOptionPane.showInputDialog("Enter tolerance (e.g. 0.0001):"));
                    int decB = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultB = BisectionMethod.solve(function, lower, upper, tolB, decB);
                    StringBuilder sbB = new StringBuilder();
                    sbB.append("Bisection Method\nFunction: f(x) = ").append(resultB.function)
                            .append("\nInitial Bounds: x0 = ").append(resultB.initialLower)
                            .append(", x1 = ").append(resultB.initialUpper).append("\n\n");
                    sbB.append(String.format("%-10s %-12s %-12s %-12s %-14s %-10s%n",
                            "Iter", "x0", "x1", "x2", "f(x2)", "Ea"));
                    for (var r : resultB.iterations) {
                        sbB.append(String.format("%-10d %-12." + decB + "f %-12." + decB + "f %-12." + decB + "f %-14." + decB + "f %-10." + decB + "f%n",
                                r.iteration, r.x0, r.x1, r.x2, r.fx2, r.ea));
                    }
                    sbB.append("\nFinal Estimated Root: ").append(String.format("%." + decB + "f", resultB.root));
                    outputArea.setText(sbB.toString());
                    break;

                case "Secant":
                    if (function.isEmpty()) {
                        outputArea.setText("Please enter a function.");
                        return;
                    }
                    double x0 = Double.parseDouble(JOptionPane.showInputDialog("Enter initial guess x0:"));
                    double x1 = Double.parseDouble(JOptionPane.showInputDialog("Enter initial guess x1:"));
                    double tolS = Double.parseDouble(JOptionPane.showInputDialog("Enter tolerance (e.g. 0.0001):"));
                    int decS = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultS = SecantMethod.solve(function, x0, x1, tolS, decS);
                    StringBuilder sbS = new StringBuilder();
                    sbS.append("Secant Method\nFunction: f(x) = ").append(resultS.function)
                            .append("\nInitial Guesses: x0 = ").append(resultS.initialX0)
                            .append(", x1 = ").append(resultS.initialX1).append("\n\n");
                    sbS.append(String.format("%-10s %-12s %-12s %-12s %-14s %-10s%n",
                            "Iter", "x0", "x1", "x2", "f(x2)", "Ea"));
                    for (var r : resultS.iterations) {
                        sbS.append(String.format("%-10d %-12." + decS + "f %-12." + decS + "f %-12." + decS + "f %-14." + decS + "f %-10." + decS + "f%n",
                                r.iteration, r.x0, r.x1, r.x2, r.fx2, r.ea));
                    }
                    sbS.append("\nFinal Root Estimate: ").append(String.format("%." + decS + "f", resultS.root));
                    outputArea.setText(sbS.toString());
                    break;

                case "Doolittle":
                    int nD = Integer.parseInt(JOptionPane.showInputDialog("Enter matrix size n (e.g., 3 for 3x3):"));
                    double[][] A = new double[nD][nD];
                    double[] bD = new double[nD]; // Renamed to bD to avoid conflict
                    for (int i = 0; i < nD; i++) {
                        for (int j = 0; j < nD; j++) {
                            A[i][j] = Double.parseDouble(JOptionPane.showInputDialog(
                                    String.format("Enter A[%d][%d]:", i, j)));
                        }
                        bD[i] = Double.parseDouble(JOptionPane.showInputDialog(
                                String.format("Enter b[%d]:", i)));
                    }
                    int decD = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultD = DoolittleMethod.solve(A, bD, decD);
                    StringBuilder sbD = new StringBuilder();
                    sbD.append("Doolittle Method\nMatrix A:\n");
                    for (int i = 0; i < nD; i++) {
                        for (int j = 0; j < nD; j++) {
                            sbD.append(String.format("%10." + decD + "f", resultD.A[i][j]));
                        }
                        sbD.append("\n");
                    }
                    sbD.append("Vector b:\n");
                    for (int i = 0; i < nD; i++) {
                        sbD.append(String.format("%10." + decD + "f", resultD.b[i])).append("\n");
                    }
                    sbD.append("\nL Matrix:\n");
                    for (int i = 0; i < nD; i++) {
                        for (int j = 0; j < nD; j++) {
                            sbD.append(String.format("%10." + decD + "f", resultD.L[i][j]));
                        }
                        sbD.append("\n");
                    }
                    sbD.append("U Matrix:\n");
                    for (int i = 0; i < nD; i++) {
                        for (int j = 0; j < nD; j++) {
                            sbD.append(String.format("%10." + decD + "f", resultD.U[i][j]));
                        }
                        sbD.append("\n");
                    }
                    sbD.append("\nSolution Vector x:\n");
                    for (int i = 0; i < nD; i++) {
                        sbD.append(String.format("x%d = %." + decD + "f\n", i, resultD.solution[i]));
                    }
                    outputArea.setText(sbD.toString());
                    break;

                case "Gauss-Seidel":
                    int nG = Integer.parseInt(JOptionPane.showInputDialog("Enter matrix size n (e.g., 3 for 3x3):"));
                    double[][] AG = new double[nG][nG];
                    double[] bG = new double[nG]; // Renamed to bG to avoid conflict
                    double[] initialGuess = new double[nG];
                    for (int i = 0; i < nG; i++) {
                        for (int j = 0; j < nG; j++) {
                            AG[i][j] = Double.parseDouble(JOptionPane.showInputDialog(
                                    String.format("Enter A[%d][%d]:", i, j)));
                        }
                        bG[i] = Double.parseDouble(JOptionPane.showInputDialog(
                                String.format("Enter b[%d]:", i)));
                        initialGuess[i] = Double.parseDouble(JOptionPane.showInputDialog(
                                String.format("Enter initial guess x%d:", i)));
                    }
                    double tolG = Double.parseDouble(JOptionPane.showInputDialog("Enter tolerance (e.g. 0.0001):"));
                    int maxIter = Integer.parseInt(JOptionPane.showInputDialog("Enter max iterations (e.g. 100):"));
                    int decG = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultG = GaussSeidelMethod.solve(AG, bG, initialGuess, tolG, maxIter, decG);
                    StringBuilder sbG = new StringBuilder();
                    sbG.append("Gauss-Seidel Method\nMatrix A:\n");
                    for (int i = 0; i < nG; i++) {
                        for (int j = 0; j < nG; j++) {
                            sbG.append(String.format("%10." + decG + "f", resultG.A[i][j]));
                        }
                        sbG.append("\n");
                    }
                    sbG.append("Vector b:\n");
                    for (int i = 0; i < nG; i++) {
                        sbG.append(String.format("%10." + decG + "f", resultG.b[i])).append("\n");
                    }
                    sbG.append("Initial Guess:\n");
                    for (int i = 0; i < nG; i++) {
                        sbG.append(String.format("x%d = %." + decG + "f\n", i, resultG.initialGuess[i]));
                    }
                    sbG.append("\nIterations:\n");
                    sbG.append(String.format("%-10s %-12s %-10s%n", "Iter", "x", "Ea"));
                    for (var r : resultG.iterations) {
                        StringBuilder xStr = new StringBuilder();
                        for (int i = 0; i < nG; i++) {
                            xStr.append(String.format("x%d=%." + decG + "f ", i, r.x[i]));
                        }
                        sbG.append(String.format("%-10d %-12s %-10." + decG + "f%n",
                                r.iteration, xStr.toString(), r.ea));
                    }
                    sbG.append("\nFinal Solution:\n");
                    for (int i = 0; i < nG; i++) {
                        sbG.append(String.format("x%d = %." + decG + "f\n", i, resultG.solution[i]));
                    }
                    outputArea.setText(sbG.toString());
                    break;

                case "Simpson's 1/3":
                    if (function.isEmpty()) {
                        outputArea.setText("Please enter a function.");
                        return;
                    }
                    double a = Double.parseDouble(JOptionPane.showInputDialog("Enter lower bound (a):"));
                    double b = Double.parseDouble(JOptionPane.showInputDialog("Enter upper bound (b):"));
                    int n = Integer.parseInt(JOptionPane.showInputDialog("Enter number of segments (n, must be even):"));
                    int decSim = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultSim = SimpsonsRule.solve(function, a, b, n, decSim);
                    StringBuilder sbSim = new StringBuilder();
                    sbSim.append("Simpson's 1/3 Rule\nFunction: f(x) = ").append(resultSim.function)
                            .append("\nInterval: [").append(resultSim.a).append(", ").append(resultSim.b)
                            .append("]\nSegments: ").append(resultSim.n).append("\n\n");
                    sbSim.append(String.format("%-12s %-14s%n", "x", "f(x)"));
                    for (int i = 0; i < resultSim.points.size(); i++) {
                        sbSim.append(String.format("%-12." + decSim + "f %-14." + decSim + "f%n",
                                resultSim.points.get(i), resultSim.values.get(i)));
                    }
                    sbSim.append("\nApproximate Integral: ").append(String.format("%." + decSim + "f", resultSim.integral));
                    outputArea.setText(sbSim.toString());
                    break;
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
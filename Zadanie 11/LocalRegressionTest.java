
import java.util.*;

public class LocalRegressionTest {
    public static void main(String[] args) {
        // Przykładowe dane (x, y)
        double[][] data = {
            {1.0, 2.1},
            {2.0, 4.0},
            {3.0, 6.1},
            {4.0, 8.0},
            {5.0, 10.1}
        };
        List<double[]> pairs = new ArrayList<>();
        for (double[] d : data) pairs.add(d);

        // Wynik lokalny (wzór)
        int N = data.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (double[] pair : data) {
            double x = pair[0];
            double y = pair[1];
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }
        double a1 = (N * sumXY - sumX * sumY) / (N * sumX2 - sumX * sumX);
        double b1 = (sumY - a1 * sumX) / N;

        // Wynik z NetLinearRegression
        double[] ab = NetLinearRegression.computeRegression(pairs);
        double a2 = ab[0];
        double b2 = ab[1];

        System.out.printf("Wzór:      a = %.6f, b = %.6f\n", a1, b1);
        System.out.printf("NetLinear: a = %.6f, b = %.6f\n", a2, b2);
        System.out.printf("Różnica:   a = %.6g, b = %.6g\n", Math.abs(a1-a2), Math.abs(b1-b2));
    }
}

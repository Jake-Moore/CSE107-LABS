package edu.jaalmoor.cse107;

import lombok.Getter;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Lab4 {
    private static final DecimalFormat DF_1 = new DecimalFormat("0.0");
    private static final DecimalFormat DF_3 = new DecimalFormat("0.000");
    private static final Random random = new Random();
    private static final int TRIALS = 10_000;
    private static final double[] pValues = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
    private static final double[] qValues = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    public static void main(String[] args) {


        Map<Pair<Double, Double>, Double> meanMap = new java.util.HashMap<>();
        Map<Pair<Double, Double>, Double> varianceMap = new java.util.HashMap<>();
        for (double p : pValues) {
            for (double q : qValues) {
                Pair<Double, Double> key = Pair.of(p, q);
                Pair<Double, Double> pair = performExperiment(p, q);
                meanMap.put(key, pair.getA());
                varianceMap.put(key, pair.getB());
            }
        }

        printTable("mean", meanMap);
        System.out.println(" ");
        printTable("variance", varianceMap);
    }
    private static void printTable(String name, Map<Pair<Double, Double>, Double> map) {
        System.out.println(name);
        System.out.println("    q:  " + String.join("     ", Arrays.stream(qValues).mapToObj(DF_1::format).toList()));
        System.out.println(" p   ------------------------------------------------------------------------");
        for (double p : pValues) {
            StringBuilder line = new StringBuilder(DF_1.format(p) + " | ");
            for (double q : qValues) {
                double mean = map.get(Pair.of(p, q));
                String prefix = (mean < 10) ? " " : "";
                line.append(prefix).append(DF_3.format(mean)).append("  ");
            }
            System.out.println(line);
        }
    }


    // Returns Pair<mean, variance>
    private static Pair<Double, Double> performExperiment(double p, double q) {
        double sum = 0;             // Sum of Y values across all trials
        double sumSquares = 0;      // Sum of Y^2 values across all trials

        for (int i = 0; i < TRIALS; i++) {
            int N = performGeometric(p);
            int Y = 0;

            // Flip Coin2 N times and sum the number of heads we get (q = probability of heads)
            for (int j = 0; j < N; j++) {
                Y += performBernoulli(q);
            }
            sum += Y;
            sumSquares += (Y * Y);
        }

        double mean = sum / TRIALS;
        double variance = (sumSquares / TRIALS) - (mean * mean);
        return Pair.of(mean, variance);
    }

    // Geometric with parameter p
    private static int performGeometric(double p) {
        int flips = 0;
        while (random.nextDouble() >= p) {
            flips++;
        }
        return flips;
    }
    // Bernoulli with parameter p
    private static int performBernoulli(double p) {
        Random random = new Random();
        return random.nextDouble() < p ? 1 : 0;
    }


    @Getter
    public static class Pair<A, B> {
        private final A a;
        private final B b;

        private Pair(A a, B b){
            this.a = a;
            this.b = b;
        }

        public static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair<>(a, b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}

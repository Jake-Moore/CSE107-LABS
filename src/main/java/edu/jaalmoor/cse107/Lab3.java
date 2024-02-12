package edu.jaalmoor.cse107;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Lab3 {
    public static final BigDecimal ZERO = BigDecimal.ZERO;

    // Constants for the experiment
    public static final int TRIALS = 100_000;
    public static final int n = 7;
    public static final double p = 0.6;
    public static final double q = 0.7;

    // Contains the raw experimental data Map<Pair, Frequency> where Frequency is the number of times the pair occurred
    public static final Map<Pair, Integer> frequencies = new HashMap<>();

    public static void main(String[] args) {
        // Run the Trials
        for (int T = 0; T < TRIALS; T++) {
            runTrial();
        }

        // Calculate the Joint PMF
        Map<Pair, BigDecimal> jointPMF = createPMF((x, y) -> {
            // Calculates the relative frequency of the pair (x, y)
            int f = frequencies.getOrDefault(new Pair(x, y), 0);
            return BigDecimal.valueOf(f).divide(BigDecimal.valueOf(TRIALS), MathContext.DECIMAL128); // frequency / trials = relative frequency
        });
        printPmf("Joint PMF of X and Y", jointPMF);

        // Calculate the Marginal PMFs
        Map<Integer, BigDecimal> marginalX = new HashMap<>();  // Maps X to the sum of the relative frequencies in the Joint PMF
        Map<Integer, BigDecimal> marginalY = new HashMap<>();
        for (Map.Entry<Pair, BigDecimal> entry : jointPMF.entrySet()) {
            Pair pair = entry.getKey();
            BigDecimal relative = entry.getValue();
            marginalX.put(pair.x, marginalX.getOrDefault(pair.x, ZERO).add(relative));
            marginalY.put(pair.y, marginalY.getOrDefault(pair.y, ZERO).add(relative));
        }
        // We assert that the sum of the values in each Marginal PMF is exactly 1

        // Calculate the Conditional PMF of X given Y
        Map<Pair, BigDecimal> condXgivenY = createPMF((x, y) -> {
            BigDecimal relative = jointPMF.getOrDefault(new Pair(x, y), ZERO);
            BigDecimal marginal = marginalY.getOrDefault(y, ZERO);
            if (marginal.compareTo(ZERO) == 0) { return ZERO; }
            return relative.divide(marginal, MathContext.DECIMAL128);
        });
        printPmf("Conditional PMF of X given Y", condXgivenY);

        // Calculate the Conditional PMF of Y given X
        Map<Pair, BigDecimal> condYgivenX = createPMF((x, y) -> {
            BigDecimal relative = jointPMF.getOrDefault(new Pair(x, y), ZERO);
            BigDecimal marginal = marginalX.getOrDefault(x, ZERO);
            if (marginal.compareTo(ZERO) == 0) { return ZERO; }
            return relative.divide(marginal, MathContext.DECIMAL128);
        });
        printPmf("Conditional PMF of Y given X", condYgivenX);

    }

    // Run a single Trial
    private static final Random random = new Random();
    private static void runTrial() {
        int x = 0; // Plays
        int y = 0; // Wins

        // Run the weeks for this trial
        for (int week = 1; week <= n; week++) {
            // For each week, flip a coin with probability p (for if Joe plays)
            if (random.nextDouble() > p) { continue; }
            // Joe plays
            x++;

            // Flip a coin with probability q (for if Joe wins)
            if (random.nextDouble() > q) { continue; }
            // Joe wins
            y++;
        }

        // Store the frequency
        Pair pair = new Pair(x, y);
        int f = frequencies.computeIfAbsent(pair, k -> 0);
        frequencies.put(pair, f + 1);
    }




    public static Map<Pair, BigDecimal> createPMF(Calculate value) {
        Map<Pair, BigDecimal> pmf = new HashMap<>();

        // Calculate the PMF for each pair (x, y)
        for (int x = 0; x <= n; x++) {
            for (int y = 0; y <= x; y++) {
                pmf.put(new Pair(x, y), value.calculate(x, y));
            }
        }

        return pmf;
    }

    public static void printPmf(String title, Map<Pair, BigDecimal> pmf) {
        System.out.println(title);
        System.out.println("  y:   0       1       2       3       4       5       6       7    ");
        System.out.println("x ------------------------------------------------------------------");
        for (int line = 0; line <= 7; line++) {
            printLine(line, (x, y) -> pmf.getOrDefault(new Pair(x, y), ZERO));
        }
        System.out.println(" ");
    }

    public static final DecimalFormat DF_4 = new DecimalFormat("0.0000");
    public static void printLine(int x, Calculate calculate) {
        StringBuilder line = new StringBuilder(x + " |  ");
        for (int y = 0; y <= x; y++) {
            line.append(DF_4.format(calculate.calculate(x, y))).append("  ");
        }
        System.out.println(line);
    }





    public interface Calculate {
        BigDecimal calculate(int x, int y);
    }
    public static class Pair {
        public int x;
        public int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || getClass() != obj.getClass()) { return false; }
            Pair pair = (Pair) obj;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x) + Integer.hashCode(y);
        }
    }
}

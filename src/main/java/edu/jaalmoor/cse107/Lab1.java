package edu.jaalmoor.cse107;

import java.text.DecimalFormat;
import java.util.Random;

public class Lab1 {
    private static final DecimalFormat DF_3_DIGITS = new DecimalFormat("0.000");
    // use one instance so we can expect uniformity of results
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        long n = 300;
        long trials = 100_000;
        Double[] headChances = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8};


        System.out.println("--------------------------");
        System.out.println(" p\t\trelative frequency");
        System.out.println("--------------------------");

        for (double headChance : headChances) {
            long numberTrialsBobTossedMoreHeads = 0;
            for (long i = 0; i < trials; i++) {
                if (performTrial(n, headChance)) {
                    numberTrialsBobTossedMoreHeads++;
                }
            }

            double relativeFrequency = (double) numberTrialsBobTossedMoreHeads / (double) trials;
            System.out.println(" " + headChance + "\t" + DF_3_DIGITS.format(relativeFrequency));
        }

    }

    /**
     * Returns IF Bob tossed more heads than Alice
     */
    private static boolean performTrial(long n, double headSuccess) {
        // Bob tosses n + 1 coins
        long bobHead = headsInNFlips(n + 1, headSuccess);

        // Alice tosses n coins
        long aliceHeads = headsInNFlips(n, headSuccess);

        return bobHead > aliceHeads;
    }

    private static long headsInNFlips(long n, double headSuccess) {
        long heads = 0;
        for (long i = 0; i < n; i++) {
            if (RANDOM.nextDouble() < headSuccess) {
                heads++;
            }
        }
        return heads;
    }
}
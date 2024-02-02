package edu.jaalmoor.cse107;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

public class Lab2 {
    private static final DecimalFormat DF_4_DIGITS = new DecimalFormat("0.0000");
    // use one instance so we can expect uniformity of results
    private static final Random RANDOM = new Random();
    private static final long totalBalls = 100;
    private static final long[] azureBalls = {10, 20, 30, 40, 50};
    private static final long trialsPerA = 2_000;

    public enum BallColor {
        AZURE, CARMINE
    }

    public static void main(String[] args) {

        System.out.println("----------------------------------------------");
        System.out.println(" #azure  #carmine   proportion ending in azure");
        System.out.println("----------------------------------------------");

        for (long a : azureBalls) {
            long c = totalBalls - a;

            long trialsWithLastBallAzure = 0;
            for (int i = 0; i < trialsPerA; i++) {
                if (runTrial(a, c)) {
                    trialsWithLastBallAzure++;
                }
            }

            double prop = (double) trialsWithLastBallAzure / trialsPerA;
            System.out.println("   " + a + "      " + c + "        " + DF_4_DIGITS.format(prop));
        }
    }

    // Return true IFF the last ball in this trial was an azure ball
    private static boolean runTrial(long a, long c) {
        List<BallColor> urn = createUrn(a, c);

        // Set the 'last' seen ball of this chapter to null
        @Nullable BallColor lastBall = null;

        // Run 'chapters' of selection until we find one to re-place
        while (!urn.isEmpty()) {
            // Set the 'last' seen ball of this chapter to null
            lastBall = null;

            // Logic for this specific chapter
            while (!urn.isEmpty()) {
                // Fetch a random ball from the urn
                int i = getRandomBallIndex(urn);
                BallColor ball = urn.get(i);

                // If extracted ball is 1st, or matches previous ball, we discard it
                if (lastBall == null || ball == lastBall) {
                    urn.remove(i);
                    lastBall = ball;
                    continue;
                }

                // If extracted ball is different from previous, we re-place it (do nothing and start new chapter)
                break;
            }
        }

        return lastBall == BallColor.AZURE;
    }

    // A non-random non-shuffled list of balls
    private static List<BallColor> createUrn(long a, long c) {
        List<BallColor> urn = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            urn.add(BallColor.AZURE);
        }
        for (int i = 0; i < c; i++) {
            urn.add(BallColor.CARMINE);
        }
        return urn;
    }

    // Return the index of a random ball in the urn
    private static int getRandomBallIndex(List<BallColor> urn) {
        return RANDOM.nextInt(urn.size());
    }
}

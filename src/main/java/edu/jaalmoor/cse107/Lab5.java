package edu.jaalmoor.cse107;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Lab5 {
    private static final Random random = new Random();
    private static final int TRIALS = 20_000;
    private static final DecimalFormat DF_1 = new DecimalFormat("0.0");
    private static final DecimalFormat DF_4 = new DecimalFormat("#.0000");

    public static void main(String[] args) {
        Map<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < TRIALS; i++) {
            String Z = runTrial();
            countMap.put(Z, countMap.getOrDefault(Z, 0) + 1);
        }
        double[] integers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] tenths = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

        System.out.println(" ");
        System.out.println("Sum of Exponentials CDF:");
        System.out.println("       .0      .1      .2      .3      .4      .5      .6      .7      .8      .9");
        System.out.println("     --------------------------------------------------------------------------------");

        for (double i : integers) {
            System.out.print(DF_1.format(i) + " | ");

            for (double tenth : tenths) {
                double z = i + tenth;
                double lessThanZ = 0;
                for (double d = 0D; d <= z; d += 0.1) {
                    String key = DF_1.format(d);
                    lessThanZ += countMap.getOrDefault(key, 0);
                }
                double cdf = lessThanZ / TRIALS;
                System.out.print(DF_4.format(cdf) + "   ");
            }
            System.out.println();
        }
    }

    // Exponential distribution sampling using inverse transform method
    public static double sample(double lambda) {
        double x = random.nextDouble();
        return -Math.log(1 - x) / lambda;
    }

    public static String runTrial() {
        double y1 = sample(1);
        double y2 = sample(1);
        int N = (int) Math.floor((y1 + y2) * 10);
        return DF_1.format(N / 10D);
    }
    /**
     *
     * Sum of Exponentials CDF:
     *        .0      .1      .2      .3      .4      .5      .6      .7      .8      .9
     *      --------------------------------------------------------------------------------
     * 0.0 | .0046   .0166   .0376   .0376   .0921   .1247   .1582   .1921   .2281   .2655
     * 1.0 | .3036   .3371   .3733   .4088   .4088   .4445   .4764   .5097   .5381   .5673
     * 2.0 | .5969   .6225   .6481   .6718   .6935   .7137   .7345   .7517   .7692   .7863
     * 3.0 | .8022   .8160   .8300   .8433   .8555   .8652   .8740   .8833   .8917   .8998
     * 4.0 | .9066   .9143   .9210   .9261   .9366   .9414   .9458   .9495   .9536   .9576
     * 5.0 | .9606   .9638   .9667   .9697   .9718   .9745   .9763   .9783   .9800   .9819
     * 6.0 | .9837   .9848   .9861   .9875   .9881   .9892   .9900   .9906   .9912   .9921
     * 7.0 | .9927   .9935   .9940   .9943   .9946   .9950   .9957   .9961   .9966   .9968
     * 8.0 | .9971   .9974   .9977   .9979   .9980   .9981   .9982   .9983   .9986   .9987
     * 9.0 | .9988   .9988   .9990   .9992   .9993   .9993   .9994   .9994   .9996   .9996
     */
}

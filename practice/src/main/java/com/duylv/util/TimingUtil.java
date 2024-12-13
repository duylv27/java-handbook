package com.duylv.util;

public final class TimingUtil {

    private TimingUtil() {}

    public static void timeThis(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

}

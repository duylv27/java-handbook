package com.duylv.service;

import java.util.concurrent.atomic.AtomicInteger;

public class JobService implements Runnable {

    private final String jobId;
    private static int smellCounter = 0;
    private static volatile int smellCounterVolatile = 0;
    private static AtomicInteger counter = new AtomicInteger(0);

    public JobService(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public void run() {
        System.out.printf("%s: Execute Job: %s%n", Thread.currentThread().getName(), this.jobId);
        try {
            // Multiple operations on atomic
            counter.incrementAndGet();
            counter.incrementAndGet();
            counter.incrementAndGet();
            counter.incrementAndGet();

            // Multiple operations on static variable
            smellCounter++;
            smellCounter++;
            smellCounter++;
            smellCounter++;

            // Multiple operations on volatile variable
            smellCounterVolatile++;
            smellCounterVolatile++;
            smellCounterVolatile++;
            smellCounterVolatile++;

            if (counter.get() != smellCounter) {
                System.out.printf("Race-Condition on static: %s - %s %n", counter, smellCounter);
            }

            if (counter.get() != smellCounterVolatile) {
                System.out.printf("Race-Condition on Volatile: %s - %s %n", counter, smellCounterVolatile);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getCounter() {
        return counter.get();
    }
}

package com.duylv.service;

import java.util.concurrent.atomic.AtomicInteger;

public class JobService implements Runnable {

    private final String jobId;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static int smellCounter = 0;

    public JobService(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public void run() {
        System.out.printf("%s: Execute Job: %s%n", Thread.currentThread().getName(), this.jobId);
        try {
            counter.incrementAndGet();
            counter.incrementAndGet();
            counter.incrementAndGet();
            counter.incrementAndGet();
            smellCounter++;
            smellCounter++;
            smellCounter++;
            smellCounter++;
            if (counter.get() != smellCounter) {
                System.out.printf("Race-Condition: %s - %s %n", counter, smellCounter);
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

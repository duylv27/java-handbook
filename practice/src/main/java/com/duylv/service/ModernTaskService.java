package com.duylv.service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ModernTaskService {

    private final int worker;
    private final AtomicInteger atomicWorker;
    private final List<JobService> jobServices;
    private final ExecutorService executorService;

    public ModernTaskService(int worker, List<JobService> jobServices) {
        this.worker = worker;
        this.atomicWorker = new AtomicInteger(worker);
        this.jobServices = jobServices;
        this.executorService = Executors.newFixedThreadPool(worker);
    }

    public void doJob() {
        try {
            System.out.printf("Processing job with '%s' workers\n", worker);
            BlockingQueue<JobService> blockingQueue = new LinkedBlockingQueue<>(jobServices.size());
            for (JobService service : jobServices) {
                blockingQueue.put(service);
            }

            for (int i = 0; i < jobServices.size(); i++) {
                JobService jobService = null;
                try {
                    jobService = blockingQueue.take();
                    JobService finalJobService = jobService;
                    // Multiple thread execution happens here
                    executorService.submit(() -> {
                        try {
                            finalJobService.run();
                        } finally {
                            atomicWorker.decrementAndGet();
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (jobService == null) {
                    System.out.println("No more jobs to process.");
                }
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.duylv.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NormalTaskService {

    private final List<JobService> jobServices;
    private final ExecutorService executorService;

    public NormalTaskService(int worker, List<JobService> jobServices) {
        this.jobServices = jobServices;
        this.executorService = Executors.newFixedThreadPool(worker);
    }

    public void doJob() throws InterruptedException {
        for (JobService jobService : jobServices) {
            executorService.submit(jobService);
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        // Issue: No way to track whether all jobs were completed
        System.out.println("All jobs completed (or not).");
    }

}

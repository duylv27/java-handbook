package com.duylv;

import com.duylv.service.JobService;
import com.duylv.service.ModernTaskService;
import com.duylv.service.TaskService;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<JobService> jobServices = new ArrayList<>();
        for (int i = 0; i <= 10_000; i++) {
            jobServices.add(new JobService(String.valueOf(i)));
        }
        runModernJob(jobServices);
        System.out.println("Done");
    }

    private static void runModernJob(List<JobService> jobServices) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        new ModernTaskService(250, jobServices).doJob();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    private static void runNormalJob(List<JobService> jobServices) throws InterruptedException {
        long startTime2 = System.currentTimeMillis();
        new TaskService(250, jobServices).doJob();
        long endTime2 = System.currentTimeMillis();
        long executionTime2 = endTime2 - startTime2;
        System.out.println("Execution time: " + executionTime2 + " milliseconds");
    }

}
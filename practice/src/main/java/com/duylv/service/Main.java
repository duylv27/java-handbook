package com.duylv.service;

import com.duylv.util.TimingUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int WORKER = 250;
    public static final int NUM_OF_JOB = 10_000;

    public static void main(String[] args) {
        List<JobService> jobServices = new ArrayList<>();
        for (int i = 0; i <= NUM_OF_JOB; i++) {
            jobServices.add(new JobService(String.valueOf(i)));
        }

        TimingUtil.timeThis(() -> new ModernTaskService(WORKER, jobServices).doJob());
        TimingUtil.timeThis(() -> new NormalTaskService(WORKER, jobServices).doJob());

        System.out.println("Done");
    }

}
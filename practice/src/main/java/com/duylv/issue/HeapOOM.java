package com.duylv.issue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HeapOOM {

    public static void main(String[] args) {
        if (args.length == 0) return;

        Map<String, Runnable> controllerMap = new HashMap<>();
        controllerMap.put("0", HeapOOM::issuerMemoryController);
        controllerMap.put("1", HeapOOM::modernMemoryController);

        Runnable controller = controllerMap.get(args[0]);
        if (controller != null) {
            controller.run();
        } else {
            System.out.println("Invalid argument. Expected '0' or '1'.");
        }
    }

    /**
     * Load 100.000 records synchronously. Potentially throw OutOfMemory Errors
     */
    private static void issuerMemoryController() {
        int i = 0;
        try {
            // typically, can replace by concurrent hash map
            Map<Integer, Person> personMap = new HashMap<>();
            while (i <= 100_000) {
                personMap.put(i, new Person());
                i++;
            }
        } finally {
            System.out.println("Process item: " + i);
        }
    }

    /**
     * Load 100.000 records by chunk (10.000/chunk), each chunk will be taken care by an ExecutorService
     */
    private static void modernMemoryController() {
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.printf("Processors with '%s'%n", processors);
        AtomicInteger i = new AtomicInteger(0);
        try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            Map<Integer, Person> personMap = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                executorService.submit(() -> {
                    try {
                        for (int k = 0; k < 10_000; k++) {
                            var temp = i.incrementAndGet();
                            personMap.put(temp, new Person());
                        }
                    } finally {
                        System.out.printf("Thread '%s' complete - Total processed: '%s'%n", Thread.currentThread().getName(), i.get());
                    }
                });
            }
            awaitTermination(executorService);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Process item: " + i.get());
    }

    private static void awaitTermination(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
        }
    }

}

record Person() {}

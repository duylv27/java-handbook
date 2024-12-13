package com.duylv.nonblocking;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureStuff {

    public static void main(String[] args) {
        processingTime(FutureStuff::asyncBusiness);
        processingTime(FutureStuff::syncBusiness);
    }

    private static void processingTime(Runnable task) {
        System.out.printf("=== Start sync processing ===%n");
        Instant start = Instant.now();
        task.run();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.printf("=== Processing time: %sms ===%n", timeElapsed);
    }

    public static void syncBusiness() {
        System.out.println(fetchFile());
        System.out.println(doSmth());
        System.out.println("Sending mail");
    }

    public static void asyncBusiness() {
        var f1 = CompletableFuture.runAsync(() -> System.out.printf("* Fetch file %s%n", fetchFile()));
        var f2 = CompletableFuture.runAsync(() -> System.out.printf("* Do something %s%n", doSmth()));
        var completableFutures = CompletableFuture.allOf(f1, f2).thenAccept(__ -> System.out.println("* Sending mail"));
        completableFutures.join();
    }

    public static Future<String> calculateAsync() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            executorService.submit(() -> {
                Thread.sleep(1000);
                completableFuture.complete("done");
                return null;
            });
        } catch (Exception e) {
            completableFuture.completeExceptionally(e);
        } finally {
            executorService.shutdown();
        }

        return completableFuture;
    }

    private static File fetchFile() {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new File("hello.csv");
    }

    private static String doSmth() {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "successfully";
    }

}

record File(String name){}

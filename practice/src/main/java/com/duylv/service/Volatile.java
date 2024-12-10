package com.duylv.service;

public class Volatile {

    private static int number;
    private volatile static boolean ready;

    private static class Reader extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }

            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Reader().start();
        ready = true;
        number = 42;
    }
}
package com.app.PortScanner;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<PortScanner> threads = new ArrayList<>();
        for (int i = 1; i <= 65535; i++) {
            threads.add(new PortScanner(i));
        }
        runBatch(threads);
        Thread.sleep(2000);
        for (PortScanner thread : threads) {
            String status = thread.getStatus();
            String port = thread.getPort();
            if (status.equals("UNKNOWN") || status.equals("OPEN")) {
                System.out.println(port + ": " + status);
            }
        }
        System.out.println("SCAN COMPLETE");
    }

    public static void runBatch(ArrayList<PortScanner> threads) throws InterruptedException {
        int counter = 0;
        int batchNum = 1;
        for (PortScanner thread : threads) {
            if (counter == 0) { System.out.println("SCANNING BATCH " + batchNum); }
            thread.start();
            counter++;
            if (counter == 10000) {
                Thread.sleep(2000);
                counter = 0;
                batchNum++;
            }
        }
    }
}
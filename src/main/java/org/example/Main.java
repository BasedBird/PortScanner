package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<PortScanner> threads = new ArrayList<>();
        for (int i = 1; i <= 65535; i++) {
            threads.add(new PortScanner(i));
        }
        for (PortScanner thread : threads) {
            thread.start();
        }
        System.out.println("WAITING...");
        Thread.sleep(5000);
        for (PortScanner thread : threads) {
            String status = thread.getStatus();
            String port = thread.getPort();
            if (status.equals("UNKNOWN") || status.equals("OPEN")) {
                System.out.println(port + ": " + status);
            }
        }
        System.out.println("SCAN COMPLETE");

    }

}
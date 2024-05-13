package com.app.PortScanner;

import java.util.ArrayList;

public class PortScanner {

    private final String IP;
    private ArrayList<Integer> interestingPorts;

    private static final int NUM_PORTS = 65535;
    private static final int TIMEOUT = 2000;
    public static boolean IN_PROGRESS = false;

    /**
     * Basic constructor
     * @param IP the IP address to scan
     */
    PortScanner(String IP) {
        this.IP = IP;
        this.interestingPorts = new ArrayList<Integer>();
    }

    /**
     * Create and execute PortScan threads
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        PortScanner.IN_PROGRESS = true;
        ArrayList<PortScannerThread> threads = new ArrayList<>();
        for (int i = 1; i <= NUM_PORTS; i++) {
            threads.add(new PortScannerThread(this.IP, i));
        }
        runBatch(threads);
        Thread.sleep(TIMEOUT);
        for (PortScannerThread thread : threads) {
            String status = thread.getStatus();
            String port = thread.getPort();
            if (status.equals("UNKNOWN") || status.equals("OPEN")) {
                System.out.println(port + ": " + status);
                this.interestingPorts.add(Integer.parseInt(port));
            }
        }
        PortScanner.IN_PROGRESS = false;
        System.out.println("SCAN COMPLETE");
    }

    /**
     * Run threads in batches of 100,000 to avoid buffer overflow errors
     * @param threads list of PortScan threads
     * @throws InterruptedException
     */
    public void runBatch(ArrayList<PortScannerThread> threads) throws InterruptedException {
        int counter = 0;
        int batchNum = 1;
        for (PortScannerThread thread : threads) {
            if (counter == 0) { System.out.println("SCANNING BATCH " + batchNum); }
            thread.start();
            counter++;
            if (counter == 10000) {
                Thread.sleep(TIMEOUT);
                counter = 0;
                batchNum++;
            }
        }
    }

    /**
     * Parses scanned ports and returns open ports as a cleaner string
     * @return list of open ports
     */
    public String getInterestingPorts() {
        StringBuilder ret = new StringBuilder();
        for (Integer port : this.interestingPorts) {
            ret.append(port.toString()).append("<br>");
        }
        return ret.toString();
    }
}
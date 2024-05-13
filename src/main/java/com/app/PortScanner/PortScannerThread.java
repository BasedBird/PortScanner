package com.app.PortScanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PortScannerThread implements Runnable {

    private final String IP;
    private final int PORT_NUM;
    private String status;
    private Thread thread;

    /**
     * Constructor for PortScanner thread
     * @param ip the IP address to scan
     * @param port the port to scan
     */
    PortScannerThread(String ip, int port){
        this.IP = ip;
        this.PORT_NUM = port;
        this.status = "UNKNOWN";
    }

    PortScannerThread(String IP, String PORT_NUM){
        this(IP, Integer.parseInt(PORT_NUM));
    }

    /**
     * Scans the specified port, status is set to OPEN if the open was open, and TIMEOUT if the connection times out
     * @throws IOException
     */
    private void scanPort() throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(IP, PORT_NUM), 2000);
            this.status = "OPEN";
        } catch (SocketTimeoutException e) {
            this.status = "TIMEOUT";
        }
        socket.close();
    }

    /**
     * Runs the scanPort method
     */
    @Override
    public void run() {
        try {
            this.scanPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates new Thread object and begins scanning port
     */
    public void start() {
        if (this.thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public String getStatus() {
        return this.status;
    }

    public String getPort() {
        return Integer.toString(this.PORT_NUM);
    }
}

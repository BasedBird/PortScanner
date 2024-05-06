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

    PortScannerThread(String ip, int port){
        this.IP = ip;
        this.PORT_NUM = port;
        this.status = "UNKNOWN";
    }

    PortScannerThread(String IP, String PORT_NUM){
        this(IP, Integer.parseInt(PORT_NUM));
    }

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

    @Override
    public void run() {
        try {
            this.scanPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

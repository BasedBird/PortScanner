package com.app.PortScanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;

public class PortScanner implements Runnable {
    static final String appPropsPath = "src/main/resources/app.properties";
    static Properties appProps;
    static String ip;
    static {
        try {
            loadProps();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final int port;
    private String status;
    private Thread thread;

    PortScanner(int port){
        this.port = port;
        this.status = "UNKNOWN";
    }

    PortScanner(String port){
        this(Integer.parseInt(port));
    }


    private void scanPort() throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 2000);
            socket.close();
            this.status = "OPEN";
        } catch (SocketTimeoutException e) {
            this.status = "TIMEOUT";
        }
    }

    private static void loadProps() throws IOException {
        appProps = new Properties();
        appProps.load(new FileInputStream(appPropsPath));
        ip = appProps.getProperty("IP");
        System.out.println("Properties successfully loaded");
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
        return Integer.toString(this.port);
    }
}

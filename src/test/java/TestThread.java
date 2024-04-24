public class TestThread implements Runnable {

    private Thread thread;
    private String threadName;

    TestThread(String threadName){
        this.threadName = threadName;
        System.out.println("Creating new thread " + threadName);
    }

    @Override
    public void run() {
        System.out.println("Running " + this.threadName);
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: " + this.threadName + "-" + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread: " + this.threadName + " INTERRUPTED");
        }
        System.out.println("Thread: " + this.threadName + " COMPLETED");
    }

    public void start() {
        if (this.thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}

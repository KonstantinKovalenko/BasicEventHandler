package eventhandler;

import java.util.concurrent.*;

public class Process {

    public static ExecutorService execProduce = Executors.newCachedThreadPool();
    public static ExecutorService execHandler = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        Producer producer = new Producer();
        EventHandler eventHandler = new EventHandler();
        producer.addObserver(eventHandler);
        for (int i = 0; i < 2; i++) {
            execProduce.execute(producer);
        }
        execHandler.execute(eventHandler);
        while (true) {
            if (eventHandler.getQueueSize() > 10000) {
                System.out.println(eventHandler.getQueueSize());
                execProduce.shutdownNow();
                execHandler.shutdown();
                break;
            }
        }
    }
}

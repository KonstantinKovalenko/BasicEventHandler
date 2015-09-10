package eventhandler;

import java.util.concurrent.*;

public class Process {

    private static ExecutorService execProduce = Executors.newCachedThreadPool();
    private static ExecutorService execHandler = Executors.newCachedThreadPool();
    private static ExecutorService execConnector = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        Producer producer = new Producer();
        Connector connector = new Connector();
        EventHandler eventHandler = new EventHandler();
        connector.addObserver(eventHandler);
        for (int i = 0; i < 2; i++) {
            execProduce.execute(producer);
        }
        execConnector.execute(connector);
        execHandler.execute(eventHandler);
        while (true) {
            if (eventHandler.getQueueSize() > 10000) {
                execProduce.shutdownNow();
                execConnector.shutdownNow();
                execHandler.shutdown();
                break;
            }
        }
    }
}

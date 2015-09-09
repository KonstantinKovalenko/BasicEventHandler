package eventhandler;

import java.util.concurrent.*;

public class Process {

    public static ExecutorService execProduce = Executors.newCachedThreadPool();
    public static ExecutorService execHandler = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Producer producer = new Producer();

        for (int i = 0; i < 2; i++) {
            execProduce.execute(producer);
        }
        EventHandler eventHandler = new EventHandler(producer);
        execHandler.execute(eventHandler);
        execHandler.shutdown();
        while (true) {                              //если не останавливать производителей, у мя комп потом виснуть начинает)
            if (producer.getQueueSize() > 100000) {
                execProduce.shutdownNow();
                break;
            }
        }
    }
}

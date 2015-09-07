package eventhandler;

import java.util.*;
import java.util.concurrent.*;
import static mytoys.Print.*;

class Producer extends Thread {

    private static long counter = 0;
    private final long id = counter++;
    private volatile boolean finish = false;
    private EventHandler eh = new EventHandler();

    public void finishIt() {
        finish = true;
    }

    private Event throwNewEvent() {
        int randomInt = (int) (Math.random() * 3);
        switch (randomInt) {
            case 0:
                return new LowSpeedEvent();
            case 1:
                return new MediumSpeedEvent();
            case 2:
                return new HighSpeedEvent();
            default:
                return null;
        }
    }

    private void fillQueue(Queue queue) {
        try {
            TimeUnit.SECONDS.sleep(3);
            queue.offer(throwNewEvent());
        } catch (InterruptedException ignored) {

        }
    }

    @Override
    public void run() {
        while (true) {
            if (!finish) {
                fillQueue(eh.queue);
            } else {
                println("Producer " + id + " interrupted");
                return;
            }
        }
    }
}

class Event {

    public void executeEvent() {

    }
}

class LowSpeedEvent extends Event {

    private static long counter = 0;
    private final long id = counter++;
    private final Map<Integer, Integer> workMap = new HashMap<>();

    @Override
    public void executeEvent() {
        try {
            TimeUnit.SECONDS.sleep(7);
            for (int i = 0; i < 1000000; i++) {
                workMap.put(i, (int) (Math.random() * i));
            }
        } catch (InterruptedException ignored) {

        } finally {
            println("LowSpeedEvent " + id + " ended");
        }
    }
}

class MediumSpeedEvent extends Event {

    private static long counter = 0;
    private final long id = counter++;
    private final Map<Integer, Integer> workMap = new HashMap<>();

    @Override
    public void executeEvent() {
        try {
            TimeUnit.SECONDS.sleep(5);
            for (int i = 0; i < 100000; i++) {
                workMap.put(i, (int) (Math.random() * i));
            }
        } catch (InterruptedException ignored) {

        } finally {
            println("MediumSpeedEvent " + id + " ended");
        }
    }
}

class HighSpeedEvent extends Event {

    private static long counter = 0;
    private final long id = counter++;
    private final Map<Integer, Integer> workMap = new HashMap<>();

    @Override
    public void executeEvent() {
        try {
            TimeUnit.SECONDS.sleep(3);
            for (int i = 0; i < 10000; i++) {
                workMap.put(i, (int) (Math.random() * i));
            }
        } catch (InterruptedException ignored) {

        } finally {
            println("HighSpeedEvent " + id + " ended");
        }
    }
}

public class EventHandler {

    private static Producer t;
    static volatile Queue<? extends Event> queue = new LinkedList<>();
    private static List<Producer> producerList = new ArrayList<>();

    public static void startProducers(int countOfProducers) {
        for (int i = 0; i < countOfProducers; i++) {
            try {
                t = new Producer();
                producerList.add(t);
                t.start();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {

            }
        }
    }

    public static void handleEvents() {
        try {
            while (true) {
                if (!Thread.interrupted()) {
                    if (queue.peek() != null) {
                        queue.remove().executeEvent();
                    }
                    if (queue.size() > 100) {
                        Thread.currentThread().interrupt();
                        for (Producer p : producerList) {
                            p.finishIt();
                        }
                    }
                } else {
                    return;
                }
            }
        } finally {
            println("Service stopped, ending queue events");
            while (queue.peek() != null) {
                queue.remove().executeEvent();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        startProducers(5);
        handleEvents();
    }
}

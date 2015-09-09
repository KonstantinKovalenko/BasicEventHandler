package eventhandler;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class EventHandler extends Thread implements Observer {

    private static Logger log = Logger.getLogger(EventHandler.class.getName());
    private long counter = 0;
    private volatile Queue<Event> eventQueue = new LinkedList<>();

    @Override
    public void update(Queue<Event> queue) {
        while (queue.peek() != null) {
            eventQueue.offer(queue.remove());
        }
    }

    public int getQueueSize() {
        return eventQueue.size();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (eventQueue.peek() != null) {
                    eventQueue.remove().execute();
                    System.out.println("Event ended: " + counter++);
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        if (eventQueue.peek() == null) {
                            break;
                        }
                    } catch (InterruptedException ie) {
                        log.log(Level.SEVERE, "InterruptedException", ie);
                    }
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Queue.size() is " + eventQueue.size() + ", EventHandler has stop");
                    System.out.println("Ending EventHandler's queue");
                    break;
                }
            }
        } finally {
            while (eventQueue.peek() != null) {
                eventQueue.remove().execute();
                System.out.println("Event ended: " + counter++);
            }
        }
    }
}

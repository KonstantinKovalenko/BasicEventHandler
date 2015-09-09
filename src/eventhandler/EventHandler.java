package eventhandler;

import java.util.*;

public class EventHandler implements Observer,Runnable {

    private long receivedCounter = 0;
    private long counter = 0;
    private volatile Queue<Event> eventQueue = new LinkedList<>();
    private Producer producer;

    EventHandler(Producer producer) {
        this.producer = producer;
        producer.addObserver(this);
    }

    @Override
    public void update(Queue<Event> queue) {
        while (queue.peek() != null) {
            eventQueue.offer(queue.remove());
            System.out.println("received event " + receivedCounter++);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (eventQueue.peek() != null) {
                    eventQueue.remove().execute();
                    System.out.println("Event ended: " + counter++);
                }
                if (eventQueue.size() > 20000) {
                    producer.removeObserver(this);
                    System.out.println("Queue.size() is " + eventQueue.size() + ", EventHandler has stop");
                    System.out.println("Ending EventHandler's queue");
                    return;
                }
            }
        } finally {
            System.err.println("Finally block");
            while (eventQueue.peek() != null) {
                eventQueue.remove().execute();
                System.out.println("Event ended: " + counter++);
            }
        }
    }
}

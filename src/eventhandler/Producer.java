package eventhandler;

import java.util.*;

public class Producer extends Thread {

    private static Queue<Event> eventQueue = new LinkedList<>();

    private Event throwNewEvent() {
        int randomInt = (int) (Math.random() * 3);
        switch (randomInt) {
            case 0:
                return new Event(0);
            case 1:
                return new Event(1);
            case 2:
                return new Event(2);
            default:
                return new Event(0);
        }
    }

    public Queue getQueue() {
        return eventQueue;
    }

    private void fillQueue(Queue<Event> queue) {
        queue.offer(throwNewEvent());
    }

    public void run() {
        while (true) {
            if (!Thread.currentThread().isInterrupted()) {
                fillQueue(eventQueue);
                Thread.yield();
            } else {
                System.out.println("Producer shutted down");
                return;
            }
        }
    }
}

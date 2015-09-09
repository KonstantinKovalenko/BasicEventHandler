package eventhandler;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Producer extends Thread implements Observable {

    static Logger log = Logger.getLogger(Producer.class.getName());
    private static List<Observer> observerList = new ArrayList<>();
    private static volatile Queue<Event> eventQueue = new LinkedList<>();

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

    private void fillQueue(Queue<Event> queue) {
        queue.offer(throwNewEvent());
        notifyObservers();
    }

    public long getQueueSize() {
        return eventQueue.size();
    }
    public void finishThreads(){
        
    }

    public void run() {
        while (true) {
            if (!Thread.currentThread().isInterrupted()) {
                fillQueue(eventQueue);
                Thread.currentThread().yield();
            } else {
                return;
            }
        }
    }

    @Override
    public void addObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(eventQueue);
        }
    }
}

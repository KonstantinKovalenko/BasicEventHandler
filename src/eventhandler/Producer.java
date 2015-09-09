package eventhandler;

import java.util.*;

public class Producer extends Thread implements Observable {

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

    @Override
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

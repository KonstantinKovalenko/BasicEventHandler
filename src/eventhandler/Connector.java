/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventhandler;

import java.util.*;

/**
 *
 * @author Admin
 */
public class Connector extends Thread implements Observable {

    private Queue<Event> eventQueue = new LinkedList<>();
    private List<Observer> observerList = new ArrayList<>();

    public void fillAndStartQueue(Queue<Event> queue) {
        if (queue.size() > 100) {
            while (queue.peek() != null) {
                eventQueue.offer(queue.remove());
            }
            notifyObservers();
        }
    }

    public void run() {
        while (true) {
            if (!Thread.currentThread().isInterrupted()) {
                fillAndStartQueue(new Producer().getQueue());
                Thread.yield();
            } else {
                System.out.println("Connector shutted down");
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

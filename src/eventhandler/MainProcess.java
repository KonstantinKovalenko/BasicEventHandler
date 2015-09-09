package eventhandler;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import static mytoys.Print.*;

class Producer {

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
                return new Event();
        }
    }

    public void fillQueue(Queue queue) {
        queue.offer(throwNewEvent());
    }

}

class Event {

    static Logger log = Logger.getLogger("Producer log");

    void execute() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            log.log(Level.SEVERE, "InterruptedException ", ie);
        }
    }
}

class LowSpeedEvent extends Event {

    @Override
    void execute() {
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException ie) {
            log.log(Level.SEVERE, "InterruptedException ", ie);
        } finally {
            println("LowSpeedEvent ended");
        }
    }
}

class MediumSpeedEvent extends Event {

    @Override
    void execute() {
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            log.log(Level.SEVERE, "InterruptedException ", ie);
        } finally {
            println("MediumSpeedEvent ended");
        }
    }
}

class HighSpeedEvent extends Event {

    @Override
    void execute() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException ie) {
            log.log(Level.SEVERE, "InterruptedException ", ie);
        } finally {
            println("HighSpeedEvent ended");
        }
    }
}

class EventHandler {

    private volatile boolean finish = false;
    private Queue<Event> myFinalQueue;

    public void start(Queue<Event> queue) {
        try {
            while (!finish) {
                if (queue.peek()!=null) {
                    queue.remove().execute();
                }
                if (queue.size() > 2000000) {
                    stop();
                }
            }
        } finally {
            myFinalQueue = queue;
            println("EventHandler stopped, events in queue " + myFinalQueue.size());
            while (myFinalQueue.peek() != null) {
                queue.remove().execute();
            }
        }
    }

    private void stop() {
        finish = true;
    }
}

public class MainProcess {

    private static Producer producer = new Producer();
    private static ExecutorService exec = Executors.newCachedThreadPool();
    private static Queue<Event> eventsQueue = new LinkedList();
    private static EventHandler eventHandler = new EventHandler();
    private static Thread t = new Thread() {
        public void run() {
            while (!exec.isShutdown()) {
                producer.fillQueue(eventsQueue);
            }
            if(exec.isShutdown())
                println("Thread shutted down");
        }
    };

    public static void main(String[] args) {
        for(int i = 1;i<10;i++){
            exec.execute(t);
        }
        try{TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){}
        exec.shutdownNow();
        eventHandler.start(eventsQueue);
    }
}

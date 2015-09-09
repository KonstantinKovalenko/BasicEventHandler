package eventhandler;

import java.util.concurrent.*;
import java.util.logging.*;

public class Event {

    private int sleepMS;
    static Logger log = Logger.getLogger(Event.class.getName());

    Event(int sleepMS) {
        this.sleepMS = sleepMS;
    }

    void execute() {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepMS);
        } catch (InterruptedException ie) {
            log.log(Level.SEVERE, "InterruptedException ", ie);
            System.err.println("Event interrupted");
        }
    }
}

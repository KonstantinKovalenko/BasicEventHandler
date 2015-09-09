package eventhandler;

import java.util.*;

public interface Observer {

    void update(Queue<Event> queue);
}

package eventhandler;

public interface Observable {

    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}

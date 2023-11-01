package edu.syr.task.model;

import edu.syr.task.repository.Observer;

import java.util.ArrayList;
import java.util.List;


/**
 * An observable class in the observer design pattern.
 * It maintains a list of observers and notifies them of changes.
 */
public class TaskObservable {

    /** List of observers to be notified. */
    private List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers with a message.
     *
     * @param message The message to notify the observers.
     */
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}


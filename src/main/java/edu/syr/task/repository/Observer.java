package edu.syr.task.repository;

/**
 * Represents an observer in the observer design pattern.
 * Observers get notified of changes or specific events.
 */
public interface Observer {

    /**
     * Gets called to update the observer with a message or notification.
     *
     * @param message The notification or message to be sent to the observer.
     */
    void update(String message);
}

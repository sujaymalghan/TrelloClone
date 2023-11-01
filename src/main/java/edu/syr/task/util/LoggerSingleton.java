package edu.syr.task.util;

import java.time.LocalDateTime;

/**
 * LoggerSingleton is a singleton class responsible for logging messages with a timestamp.
 *
 * <p>
 * The LoggerSingleton class follows the singleton design pattern, ensuring that only
 * one instance of the LoggerSingleton class is ever created. This instance can be
 * used throughout the application to log messages along with the current timestamp.
 * </p>
 */
public class LoggerSingleton {

    private static LoggerSingleton instance;

    /**
     * Private constructor ensures that the LoggerSingleton class cannot be instantiated from
     * outside this class.
     */
    private LoggerSingleton() {}

    /**
     * Returns the single instance of the LoggerSingleton class, creating it if it does not exist.
     *
     * @return The singleton instance of LoggerSingleton.
     */
    public static LoggerSingleton getInstance() {
        if (instance == null) {
            synchronized (LoggerSingleton.class) {
                if (instance == null) {
                    instance = new LoggerSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Logs the given message with the current timestamp.
     *
     * @param message The message to be logged.
     */
    public void log(String message) {
        System.out.println(LocalDateTime.now() + ": " + message);
    }
}

package edu.syr.task.util;

import java.time.LocalDateTime;

public class LoggerSingleton {

    private static LoggerSingleton instance;

    private LoggerSingleton() {}

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

    public void log(String message) {
        System.out.println(LocalDateTime.now() + ": " + message);
    }
}

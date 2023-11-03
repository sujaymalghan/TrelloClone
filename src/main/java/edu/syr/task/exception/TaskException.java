package edu.syr.task.exception;

/**
 * Custom exception to handle task-related errors.
 *
 * <p>
 * This exception is thrown when there's a specific error or
 * issue related to task operations or data. It extends the
 * {@link RuntimeException} which means it's an unchecked exception.
 * </p>
 */
public class TaskException extends RuntimeException {
    /**
     * Constructs a new TaskException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.

  */
    public TaskException(String message) {
        super(message);
    }
}
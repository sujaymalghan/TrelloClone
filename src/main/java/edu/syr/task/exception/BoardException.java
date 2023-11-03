package edu.syr.task.exception;

public class BoardException extends RuntimeException {
    /**
     * Constructs a new BoardException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.

     */
    public BoardException(String message) {
        super(message);
    }

}

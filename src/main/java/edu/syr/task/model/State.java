package edu.syr.task.model;

/**
 * Enumeration representing the possible states of a task.
 *
 * <p>
 * This enum provides a set of predefined constants to describe the
 * current status or progress of a task.
 * </p>
 */
public enum State {
    /** Represents a task that is yet to be started. */
    TODO,

    /** Represents a task that is currently in progress. */
    DOING,

    /** Represents a task that has been completed. */
    DONE;
}

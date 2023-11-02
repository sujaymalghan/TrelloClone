package edu.syr.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a Task entity in the MongoDB "tasks" collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    /** Unique identifier for the task. */
    @Id
    private String id;

    /** An integer identifier for the task. */
    private Integer taskid;

    /** State of the task. */
    private State state;

    /** User to whom the task is assigned. */
    private List<String> assignedTo;

    /** Description of the task. */
    private String description;

    /** List of comments associated with the task. */
    private List<String> comments;

    /** Due date for the task. */
    private String dueDate;

    /** Creation time of the task. */
    private String creationTime;

    /** Closed time of the task if applicable. */
    private String closedTime;

    /** Logs related to the task. */
    private HashMap<Integer,List<String>> logs;
}
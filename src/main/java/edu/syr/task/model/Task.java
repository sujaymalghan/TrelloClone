package edu.syr.task.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    private TaskState state;
    private String assignedTo;
    private String description;
    private List<String> comments;
    private long creationTime;

}
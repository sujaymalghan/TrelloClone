package edu.syr.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private Integer taskid;
    private State state;
    private String assignedTo;
    private String description;
    private List<String> comments;
    private String creationTime;
    private String closedTime;
    private HashMap<Integer,List<String>> logs;

}
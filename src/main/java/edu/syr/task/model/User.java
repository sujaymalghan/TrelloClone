package edu.syr.task.model;

import edu.syr.task.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a User entity in the MongoDB "users" collection.
 */
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    /** Unique identifier for the user. */
    @Id
    private String id;

    /** Name of the user. */
    private String name;

    /** Department the user belongs to. */
    private String department;

    /** List of tasks associated with the user. */
    private List<TaskDTO> tasks = new ArrayList<>();
}
package edu.syr.task.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "board")
public class Board {

    /** Unique identifier for the board. */
    @Id
    private long id;

    /** Name of the board. */
    private String name;

    /** Description of the board. */
    private String description;

    /** Lists of tasks on the board, with each list representing a stage of the project. */
    private List<Task> taskLists;

}

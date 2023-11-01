package edu.syr.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


/**
 * Represents a sequence generator entity in the MongoDB.
 * Useful for creating sequences like unique integer IDs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseSequence {

    /** Unique identifier for the sequence. */
    @Id
    private String id;

    /** Current value of the sequence. */
    private long seq;
}

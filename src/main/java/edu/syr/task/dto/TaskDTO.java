package edu.syr.task.dto;

import edu.syr.task.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object (DTO) representing a task.
 *
 * <p>
 * This DTO provides a simplified view of the task entity
 * for data transfer between layers or services. It captures
 * the key attributes of a task like its identifier and state.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private int taskid;
    private State state;

}
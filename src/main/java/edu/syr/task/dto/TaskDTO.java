package edu.syr.task.dto;

import edu.syr.task.model.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private TaskState state;
    private String assignedTo;
    private String description;
    private List<String> comments;

}

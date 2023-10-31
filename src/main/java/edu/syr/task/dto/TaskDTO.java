package edu.syr.task.dto;

import edu.syr.task.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private int taskid;
    private State state;

}

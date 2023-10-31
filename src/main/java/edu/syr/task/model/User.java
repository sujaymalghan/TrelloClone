package edu.syr.task.model;

import edu.syr.task.dto.TaskDTO;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.*;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    private ObjectId id;
    private String name;
    private  String department;

    @DBRef
    private List<TaskDTO> tasks = new ArrayList<>();


}
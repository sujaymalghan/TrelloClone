package edu.syr.task.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.*;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    private String id;
    private String name;
    private  String Department;

    @DBRef
    private List<Task> tasks = new ArrayList<>();


}
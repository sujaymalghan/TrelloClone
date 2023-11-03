package edu.syr.task.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "boardsequence")
public class BoardSequence {

    @Id
    private String id;

    private String sequenceName;

    private long sequenceValue;

}
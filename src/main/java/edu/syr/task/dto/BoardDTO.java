package edu.syr.task.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    /** Name of the board. */
    private String name;

    /** Description of the board. */
    private String description;
}

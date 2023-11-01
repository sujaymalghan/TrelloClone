package edu.syr.task.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a user.
 *
 * <p>
 * This DTO provides a simplified view of the user entity
 * for data transfer between layers or services. It mainly
 * contains basic attributes related to a user.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String name;
    private String department;

    /**
     * Provides a string representation of the user DTO.
     *
     * @return a formatted string containing the name and department of the user.
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)", name, department);
    }

}
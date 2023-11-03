package edu.syr.task.service.interfaces;

import edu.syr.task.dto.UserDTO;
import edu.syr.task.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {

    User createUser(User user);

    void deleteUser(ObjectId id);

    List<User> getAllUsers();

    List<User> findUsersByTaskId(int taskId);

    List<User> existsByName(String name);

    UserDTO convertToDTO(User user);

    List<String> findUsersByStartingLetter(String startingLetter);


}

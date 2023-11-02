package edu.syr.task.service;

import edu.syr.task.dto.TaskDTO;
import edu.syr.task.dto.UserDTO;
import edu.syr.task.model.User;
import edu.syr.task.repository.UserRepository;
import edu.syr.task.util.LoggerSingleton;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    /**
     * Singleton logger instance.
     */
    private LoggerSingleton logger = LoggerSingleton.getInstance();

    /**
     * The UserRepository instance for database operations related to users.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return The created user.
     */
    public User createUser(User user) {
        try {
            if (user.getTasks() == null) {
                user.setTasks(new ArrayList<>());
            }
            logger.log("Creating user: " + user.getName());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.log("Error while creating user: " + e.getMessage());
            throw e;
        }
    }


    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllusers() {
        try {
            logger.log("Fetching all users");
            return userRepository.findAll();
        } catch (Exception e) {
            logger.log("Error while fetching all users: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Finds users by a specific task ID.
     *
     * @param taskid The task ID to search for.
     * @return A list of users associated with the given task ID.
     */
    public List<User> findUsersByTaskId(int taskid) {
        try {
            logger.log("Finding users by task ID: " + taskid);
            List<User> allUsers = userRepository.findAll();
            List<User> usersWithTask = new ArrayList<>();

            for (User user : allUsers) {
                List<TaskDTO> userTasks = user.getTasks();
                if (userTasks != null && !userTasks.isEmpty()) {
                    for (TaskDTO task : userTasks) {
                        if (task.getTaskid() == taskid) {
                            usersWithTask.add(user);
                            break;
                        }
                    }
                }
            }
            return usersWithTask;
        } catch (Exception e) {
            logger.log("Error while finding users by task ID: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if a user exists by their name.
     *
     * @param assignedTo The name of the user.
     * @return A list of users with the given name.
     */
    public List<User> existsByName(String assignedTo) {
        try {
            logger.log("Checking if user exists by name: " + assignedTo);
            return userRepository.existsByName(assignedTo);
        } catch (Exception e) {
            logger.log("Error while checking if user exists by name: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Converts a User entity to its corresponding DTO.
     *
     * @param user The user entity.
     * @return The user DTO.
     */
    public UserDTO convertToDTO(User user) {
        try {
            UserDTO dto = new UserDTO();
            dto.setName(user.getName());
            dto.setDepartment(user.getDepartment());
            return dto;
        } catch (Exception e) {
            logger.log("Error while converting to DTO: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Finds users by the starting letter of their name.
     *
     * @param assignedTo The starting letter.
     * @return A formatted list of users' names starting with the given letter.
     */
    public List<String> findUsersByStartingLetter(String assignedTo) {
        try {
            logger.log("Finding users by starting letter: " + assignedTo);
            List<User> users = userRepository.findUsersByStartingLetter(assignedTo);
            List<UserDTO> dtos = users.stream().map(this::convertToDTO).collect(Collectors.toList());
            List<String> formattedUsers = new ArrayList<>();
            for (int i = 0; i < dtos.size(); i++) {
                formattedUsers.add("User " + (i + 1) + " " + dtos.get(i).toString());
            }
            return formattedUsers;
        } catch (Exception e) {
            logger.log("Error while finding users by starting letter: " + e.getMessage());
            throw e;
        }
    }
}
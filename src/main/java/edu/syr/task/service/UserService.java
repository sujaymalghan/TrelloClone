package edu.syr.task.service;

import edu.syr.task.dto.TaskDTO;
import edu.syr.task.dto.UserDTO;
import edu.syr.task.model.User;
import edu.syr.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }
        return userRepository.save(user);

    }

    public List<User> getAllusers() {

        return userRepository.findAll();
    }

    public List<User> findUsersByTaskId(int taskid) {

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
    }

    public List<User> existsByName(String assignedTo) {
       return  userRepository.existsByName(assignedTo);

    }
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setDepartment(user.getDepartment());

        return dto;
    }

    public List<String> findUsersByStartingLetter(String assignedTo) {


        List<User> users = userRepository.findUsersByStartingLetter(assignedTo);
        List<UserDTO> dtos = users.stream().map(this::convertToDTO).collect(Collectors.toList());

        List<String> formattedUsers = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            formattedUsers.add("User " + (i + 1) + " " + dtos.get(i).toString());
        }
        return formattedUsers;
    }


}

package edu.syr.task.service;

import edu.syr.task.dto.TaskDTO;
import edu.syr.task.model.User;
import edu.syr.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}

package edu.syr.task.service;

import edu.syr.task.model.User;
import edu.syr.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }
        return userRepository.save(user);

    }

}

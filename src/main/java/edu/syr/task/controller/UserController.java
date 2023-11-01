package edu.syr.task.controller;

import edu.syr.task.model.User;
import edu.syr.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> showUsers() {
        List<User> user = userService.getAllusers();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/taskid/{taskId}")
    public ResponseEntity<Object> findUsersByTaskId(@PathVariable int taskId) {
        List<User> users = userService.findUsersByTaskId(taskId);
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {

            return new ResponseEntity<>("No User found with taskid = " + taskId,HttpStatus.NOT_FOUND);
        }
    }





}

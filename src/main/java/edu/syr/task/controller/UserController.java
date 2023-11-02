package edu.syr.task.controller;

import edu.syr.task.model.User;
import edu.syr.task.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST Controller that provides endpoints for CRUD operations on Users.
 *
 * <p>
 * This controller handles the management of users which include creating
 * and fetching users. It also provides an endpoint to fetch users based
 * on a specific task ID. The UserController relies on the UserService
 * to carry out these operations.
 * </p>
 */
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


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable ObjectId id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
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

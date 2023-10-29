package edu.syr.task.service;


import edu.syr.task.exception.TaskException;
import edu.syr.task.model.Task;
import edu.syr.task.model.User;
import edu.syr.task.repository.TaskRepository;
import edu.syr.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static edu.syr.task.util.TaskUtil.areStringsEqualIgnoreCase;
import static edu.syr.task.util.TaskUtil.areTaskStates;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    public Task save(Task task) {
        return taskRepository.save(task);
    }


    public boolean deleteTask(int taskid) {

        Optional<Task> taskOptional = taskRepository.findByTaskid(taskid);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskRepository.delete(task);

            Optional<User> userOptional = userRepository.findByTasksId(task.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.getTasks().remove(task);
                userRepository.save(user);
            }

            return true;
        }

        return false;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public boolean modifyTask(Task taskUpdates) {
        Integer  taskId = taskUpdates.getTaskid();
        Optional<Task> optionalOriginalTask = taskRepository.findByTaskid(taskId);

        if (!optionalOriginalTask.isPresent()) {
            throw new TaskException("Task not found with ID: " + taskId);
        }

        Task originalTask = optionalOriginalTask.get();
        List<String> changes = new ArrayList<>();

        if (!areStringsEqualIgnoreCase(originalTask.getAssignedTo(), taskUpdates.getAssignedTo())) {
            changes.add("AssignedTo changed from " + originalTask.getAssignedTo() + " to " + taskUpdates.getAssignedTo() + " Time:"+ LocalDateTime.now() );
            originalTask.setAssignedTo(taskUpdates.getAssignedTo());
        }

        if (!areTaskStates(originalTask.getState(), taskUpdates.getState())) {
            changes.add("State changed from " + originalTask.getState() + " to " + taskUpdates.getState() + " Time: " + LocalDateTime.now());
            originalTask.setState(taskUpdates.getState());
        }


        originalTask.setDescription(taskUpdates.getDescription());
        List<String> originalComments = originalTask.getComments();
        List<String> commentsFromUpdatedTask = taskUpdates.getComments();

        if (commentsFromUpdatedTask != null && !commentsFromUpdatedTask.isEmpty()) {
            changes.add("Added new comments: " + commentsFromUpdatedTask);
            originalComments.addAll(commentsFromUpdatedTask);
        }
        originalTask.setComments(originalComments);
        taskRepository.save(originalTask);

        return true;
    }




}

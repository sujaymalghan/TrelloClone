package edu.syr.task.service;


import edu.syr.task.exception.TaskException;
import edu.syr.task.model.Task;
import edu.syr.task.model.User;
import edu.syr.task.repository.TaskRepository;
import edu.syr.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static edu.syr.task.util.TaskUtil.*;

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

        Task taskOptional = taskRepository.findByTaskid(taskid);

        if (taskOptional!=null) {

            taskRepository.delete(taskOptional);

            Optional<User> userOptional = userRepository.findByTasksId(taskOptional.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.getTasks().remove(taskOptional);
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
        Task optionalOriginalTask = taskRepository.findByTaskid(taskId);

        if (optionalOriginalTask==null) {
            throw new TaskException("Task not found with ID: " + taskId);
        }

        String assignedUser = taskUpdates.getAssignedTo();
        List<User> checkuser = userRepository.existsByName(assignedUser);

        if (checkuser.isEmpty()) {
            throw new TaskException("Assigned user not found in User table.");
        }

        Task originalTask = optionalOriginalTask;
        List<String> changes = new ArrayList<>();
        HashMap<Integer, List<String>> newchanges = originalTask.getAlldetails();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        if (!areStringsEqualIgnoreCase(originalTask.getAssignedTo(), taskUpdates.getAssignedTo())) {
            changes.add("AssignedTo changed from " + originalTask.getAssignedTo() + " to " + taskUpdates.getAssignedTo() + "at Time: "+ formattedDateTime );
            originalTask.setAssignedTo(taskUpdates.getAssignedTo());
        }

        if (!areTaskStates(originalTask.getState(), taskUpdates.getState())) {
            changes.add("State changed from " + originalTask.getState() + " to " + taskUpdates.getState() + " at Time: " + formattedDateTime);
            originalTask.setState(taskUpdates.getState());
        }

        if (!aredescription(originalTask.getDescription(),taskUpdates.getDescription()))
        {

            originalTask.setDescription("New Desciption: " + taskUpdates.getDescription() );

        }


        originalTask.setDescription(taskUpdates.getDescription());
        List<String> originalComments = originalTask.getComments();
        List<String> commentsFromUpdatedTask = taskUpdates.getComments();

        if (commentsFromUpdatedTask != null && !commentsFromUpdatedTask.isEmpty()) {
            changes.add("Added new comments: " + commentsFromUpdatedTask + "at Timme:  " + formattedDateTime);
            originalComments.addAll(commentsFromUpdatedTask);
        }

        originalTask.setComments(originalComments);
        changes.addAll(newchanges.get(taskId)!=null ? newchanges.get((taskId)) : Arrays.asList(""));

        HashMap<Integer,List<String>> modifiedalldetials =new HashMap<>();
        modifiedalldetials.put(taskId,changes);
        originalTask.setAlldetails(modifiedalldetials);
        taskRepository.save(originalTask);

        return true;
    }


}

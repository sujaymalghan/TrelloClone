package edu.syr.task.service;


import edu.syr.task.dto.TaskDTO;
import edu.syr.task.exception.TaskException;
import edu.syr.task.model.State;
import edu.syr.task.model.Task;
import edu.syr.task.model.User;
import edu.syr.task.repository.TaskRepository;
import edu.syr.task.repository.UserRepository;
import edu.syr.task.util.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static edu.syr.task.util.TaskUtil.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    private LoggerSingleton logger = LoggerSingleton.getInstance();
    @Autowired
    private UserRepository userRepository;
    public Task save(Task task) {


        logger.log("Saving task with ID: " + task.getTaskid());
        return taskRepository.save(task);
    }


    public boolean deleteTask(int taskid) {
        logger.log("Attempting to delete task with ID: " + taskid);

        Task taskOptional = taskRepository.findByTaskid(taskid);

        if (taskOptional!=null) {

            taskRepository.delete(taskOptional);

            Optional<User> userOptional = userRepository.findByTasksTaskid(taskOptional.getTaskid());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.getTasks().remove(taskOptional);
                userRepository.save(user);
            }
            logger.log("Successfully deleted task with ID: " + taskid);
            return true;
        }
        logger.log("Failed to delete task with ID: " + taskid);
        return false;
    }

    public List<Task> getAllTasks() {


        logger.log("Fetching all tasks");
        return taskRepository.findAll();
    }
@Transactional
    public boolean modifyTask(Task taskUpdates) {
        Integer  taskId = taskUpdates.getTaskid();

        logger.log("Attempting to modify task with ID: " + taskId);

        Task optionalOriginalTask = taskRepository.findByTaskid(taskId);



        if (optionalOriginalTask==null) {
            logger.log("Task not found with ID: " + taskId);
            throw new TaskException("Task not found with ID: " + taskId);
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



            if (taskUpdates.getState()== State.DONE)
            {
                originalTask.setClosedTime(LocalDateTime.now().format(formatter));
            }
            originalTask.setState(taskUpdates.getState());

            List<User> usersWithTask = userService.findUsersByTaskId(taskId);
            if (usersWithTask!=null) {
                for (User user : usersWithTask) {
                    List<TaskDTO> tasks = user.getTasks();
                    for (TaskDTO task : tasks) {
                        if (task.getTaskid() == taskId) {
                            task.setState(taskUpdates.getState());
                            break;
                        }
                    }
                    userRepository.save(user);
                }
            }

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
        logger.log("Successfully modified task with ID: " + taskId);
        return true;
    }


}

package edu.syr.task.service;


import edu.syr.task.dto.TaskDTO;
import edu.syr.task.exception.TaskException;
import edu.syr.task.model.State;
import edu.syr.task.model.Task;
import edu.syr.task.model.TaskObservable;
import edu.syr.task.model.User;
import edu.syr.task.repository.TaskRepository;
import edu.syr.task.repository.UserRepository;
import edu.syr.task.util.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.syr.task.util.TaskUtil.*;

@Service
public class TaskService {

    /**
     * The TaskRepository instance for task-related database operations.
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * The UserService instance for user-related operations.
     */
    @Autowired
    private UserService userService;

    /**
     * The LoggerSingleton instance for logging operations.
     */
    private LoggerSingleton logger = LoggerSingleton.getInstance();

    /**
     * The UserRepository instance for user-related database operations.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Saves the provided task to the database.
     *
     * @param task The task object to be saved.
     * @return The saved task object.
     * @throws Exception If any error occurs during the saving process.
     */
    public Task save(Task task) {
        try {
            logger.log("Saving task with ID: " + task.getTaskid());
            return taskRepository.save(task);
        } catch (Exception e) {
            logger.log("Error while saving task: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a task with the specified task ID. This method will also remove the task from any users
     * associated with that task.
     *
     * @param taskid The ID of the task to be deleted.
     * @return true if the task was successfully deleted, false otherwise.
     * @throws Exception If any error occurs during the deletion process.
     */
    public boolean deleteTask(int taskid) {
        try {
            logger.log("Attempting to delete task with ID: " + taskid);
            Task taskOptional = taskRepository.findByTaskid(taskid);
            if (taskOptional != null) {
                taskRepository.delete(taskOptional);
                List<User> usersWithTask = userRepository.findByTasksTaskid(taskid);
                for (User user : usersWithTask) {
                    user.getTasks().removeIf(task -> task.getTaskid() == taskid);
                    userRepository.save(user);
                }
                logger.log("Successfully deleted task with ID: " + taskid);
                return true;
            }
            logger.log("Failed to delete task with ID: " + taskid);
            return false;
        } catch (Exception e) {
            logger.log("Error while deleting task: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fetches all tasks from the repository.
     *
     * @return A list containing all the tasks.
     * @throws Exception If any error occurs during the fetch process.
     */
    public List<Task> getAllTasks() {
        try {
            logger.log("Fetching all tasks");
            return taskRepository.findAll();
        } catch (Exception e) {
            logger.log("Error while fetching all tasks: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Modifies the details of a task based on the provided updates. This method also handles task notifications,
     * updates associated user's tasks, logs changes, and ensures that any provided updates are valid.
     *
     * @param taskUpdates The task object containing the updates to be applied.
     * @return true if the task was successfully modified, false otherwise.
     * @throws TaskException If any error or invalid data is encountered during the modification process.
     */

    @Transactional
    public boolean modifyTask(Task taskUpdates) {
        TaskObservable taskObservable = new TaskObservable();

        try {

            Integer taskId = taskUpdates.getTaskid();

            logger.log("Attempting to modify task with ID: " + taskId);

            Task optionalOriginalTask = taskRepository.findByTaskid(taskId);

            if (optionalOriginalTask == null) {
                logger.log("Task not found with ID: " + taskId);
                throw new TaskException("Task not found with ID: " + taskId);
            }
            Task originalTask = optionalOriginalTask;
            List<String> changes = new ArrayList<>();
            HashMap<Integer, List<String>> newchanges = originalTask.getLogs();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            if (!areDueDatesSame(originalTask.getDueDate(), taskUpdates.getDueDate())) {

                if (taskUpdates.getDueDate() != null) {

                    if (LocalDate.parse(taskUpdates.getDueDate()).isAfter(LocalDate.now())) {

                        changes.add("Due date changed from " + originalTask.getDueDate()
                                + " to " + taskUpdates.getDueDate()
                                + " at Time: " + formattedDateTime);
                        originalTask.setDueDate(taskUpdates.getDueDate());
                    }
                    else {
                        throw new TaskException("\"Due Date cannot be Past Date\"");
                    }
                }


            }


            if (taskUpdates.getAssignedTo() != null && !taskUpdates.getAssignedTo().isEmpty()) {
                List<String> originalAssignedToList = originalTask.getAssignedTo();
                String updatedAssignedTo = taskUpdates.getAssignedTo().get(0); // Since there's always one value

                if (!originalAssignedToList.contains(updatedAssignedTo)) {
                    List<String> mergedAssignedToList = new ArrayList<>(originalAssignedToList);
                    mergedAssignedToList.add(updatedAssignedTo);
                    changes.add("AssignedTo " + originalAssignedToList + " to " + mergedAssignedToList + " at Time: " + formattedDateTime);
                    originalTask.setAssignedTo(mergedAssignedToList);

                    List<User> existingUsers = userService.existsByName(updatedAssignedTo.trim());
                    if (!existingUsers.isEmpty()) {
                        User assignedUser = existingUsers.get(0);
                        TaskDTO newTaskDTO = new TaskDTO(taskUpdates.getTaskid(), taskUpdates.getState());
                        assignedUser.getTasks().removeIf(task -> task.getTaskid() == newTaskDTO.getTaskid());
                        assignedUser.getTasks().add(newTaskDTO);
                        userRepository.save(assignedUser);
                    } else {
                        if (userService.findUsersByStartingLetter(updatedAssignedTo).isEmpty()) {
                            List<User> allUsers = userService.getAllusers();
                            String userListString = IntStream.range(0, allUsers.size())
                                    .mapToObj(i -> "Name " + (i + 1) + ": " + allUsers.get(i).getName() +
                                            ", Department " + ": " + allUsers.get(i).getDepartment())
                                    .collect(Collectors.joining("\n"));
                            throw new TaskException("Please Provide name from the given list: " + userListString);
                        }
                        throw new TaskException("Please Provide name from the given list" + userService.findUsersByStartingLetter(updatedAssignedTo));
                    }
                }
            }



            List<User> usersWithTask = userService.findUsersByTaskId(taskId);
            if (usersWithTask != null) {
                for (User user : usersWithTask) {
                    taskObservable.addObserver(message -> System.out.println("User " + user.getName() + " notified: " + message));
                }
            }

            if (!areTaskStates(originalTask.getState(), taskUpdates.getState())) {

                changes.add("State changed from " + originalTask.getState() + " to " + taskUpdates.getState() + " at Time:  " + formattedDateTime);

                if (taskUpdates.getState() == State.DONE) {

                    LocalDateTime creationTime = LocalDateTime.parse(originalTask.getCreationTime(), formatter);
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(creationTime, currentTime);
                    long days = duration.toDays();
                    long hours = duration.toHours() - (days * 24);
                    long minutes = duration.toMinutes() - (days * 24 * 60) - (hours * 60);
                    String durationString = String.format("%d days, %d hours, %d minutes", days, hours, minutes);
                    originalTask.setClosedTime(durationString);
                    taskObservable.notifyObservers("Task with ID " + taskId + " is now DONE.");
                } else {
                    originalTask.setClosedTime("");
                }
                originalTask.setState(taskUpdates.getState());
                if (usersWithTask != null) {
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

            if (taskUpdates.getDescription() != null && !aredescription(originalTask.getDescription(), taskUpdates.getDescription())) {
                originalTask.setDescription("New Desciption: " + taskUpdates.getDescription());
                originalTask.setDescription(taskUpdates.getDescription());
            }


            List<String> originalComments = originalTask.getComments();
            List<String> commentsFromUpdatedTask = taskUpdates.getComments();

            if (commentsFromUpdatedTask != null && !commentsFromUpdatedTask.isEmpty()) {
                changes.add("Added new comments: " + commentsFromUpdatedTask + "at Time:  " + formattedDateTime);
                originalComments.addAll(commentsFromUpdatedTask);
            }

            originalTask.setComments(originalComments);
            changes.addAll(newchanges.get(taskId) != null ? newchanges.get((taskId)) : Arrays.asList(""));
            HashMap<Integer, List<String>> modifiedalldetials = new HashMap<>();
            modifiedalldetials.put(taskId, changes);
            originalTask.setLogs(modifiedalldetials);
            taskRepository.save(originalTask);
            logger.log("Successfully modified task with ID: " + taskId);
            return true;
        } catch (Exception e) {
            logger.log("Error while modifying task: " + e.getMessage());
            throw e;
        }

    }
}

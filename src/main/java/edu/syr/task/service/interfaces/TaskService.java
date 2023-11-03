package edu.syr.task.service.interfaces;

import edu.syr.task.exception.TaskException;
import edu.syr.task.model.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task) throws Exception;

    boolean deleteTask(int taskid) throws Exception;

    List<Task> getAllTasks() throws Exception;

    boolean modifyTask(Task taskUpdates) throws TaskException;

    // Add more methods here as required by your application
}

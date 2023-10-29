package edu.syr.task.controller;

import edu.syr.task.exception.TaskException;
import edu.syr.task.model.State;
import edu.syr.task.model.Task;
import edu.syr.task.service.SequenceGeneratorService;
import edu.syr.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    private TaskService taskService;
    private SequenceGeneratorService sequenceGeneratorService;

    public TaskController(TaskService taskService, SequenceGeneratorService sequenceGeneratorService) {
        this.taskService = taskService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    // Create Task
    @PostMapping("/create")
    public ResponseEntity<Integer> createTask(@RequestBody Task newtask) {
        Task task = new Task();
        task.setTaskid((int) sequenceGeneratorService.generateSequence("taskSeqName"));
        task.setState((newtask.getState() != null) ? newtask.getState() : State.TODO);
        task.setAssignedTo(null);
        task.setCreationTime(LocalDateTime.now());
        task.setComments(newtask.getComments()!=null ? newtask.getComments():null);
        task.setDescription((newtask.getDescription() != null && !newtask.getDescription().isEmpty()) ? newtask.getDescription() : "");
        task.setAlldetails(null);
        task = taskService.save(task);
        return new ResponseEntity<>(task.getTaskid(), HttpStatus.CREATED);
    }

    // Modify Task
    @PutMapping("/modify")
    public ResponseEntity<String> modifyTask(@RequestBody Task taskUpdates) {
        if (taskUpdates.getId() == null || taskUpdates.getId().isEmpty()) {
            return new ResponseEntity<>("Task ID must be provided.", HttpStatus.BAD_REQUEST);
        }

        boolean success = taskService.modifyTask(taskUpdates);
        if (success) {
            return new ResponseEntity<>("Task modified successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to modify task.", HttpStatus.BAD_REQUEST);
        }
    }


    // Delete Task
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        boolean success = taskService.deleteTask(id);
        if (success) {
            return new ResponseEntity<>("Task deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete task.", HttpStatus.BAD_REQUEST);
        }
    }

    // Show Board
    @GetMapping("/board")
    public ResponseEntity<List<Task>> showBoard() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> handleTaskException(TaskException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}



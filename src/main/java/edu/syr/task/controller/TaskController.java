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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    @Autowired
    private TaskService taskService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    // Create Task
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody Task newtask){

        if(newtask.getDueDate()=="" || newtask.getDueDate()==null)
        {
            return new ResponseEntity<String>("Due Date cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (LocalDate.parse(newtask.getDueDate()).isBefore(LocalDate.now()))
        {
            return new ResponseEntity<String>("Due Date cannot be Past Date", HttpStatus.BAD_REQUEST);

        }

        Task task = new Task();
        task.setTaskid((int) sequenceGeneratorService.generateSequence("taskSeqName"));
        task.setState(State.TODO);
        task.setAssignedTo("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        task.setCreationTime(formattedDateTime);
        task.setComments(Arrays.asList(""));
        task.setDescription((newtask.getDescription() != null && !newtask.getDescription().isEmpty()) ? newtask.getDescription() : "");
        HashMap<Integer, List<String>> hashMap = new HashMap<>();
        hashMap.put(task.getTaskid(), Arrays.asList(" "));
        task.setLogs(hashMap);
        task.setClosedTime(null);
        task.setDueDate(newtask.getDueDate());
        task = taskService.save(task);
        return new ResponseEntity<>(task.getTaskid().toString(), HttpStatus.CREATED);
    }

    // Modify Task
    @PutMapping("/modify")
    public ResponseEntity<String> modifyTask(@RequestBody Task taskUpdates) {

        if (taskUpdates.getTaskid() == null) {
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
    @GetMapping("/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> handleTaskException(TaskException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }




}



package edu.syr.task;

import edu.syr.task.controller.TaskController;
import edu.syr.task.model.Task;
import edu.syr.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class TaskApplicationTests {

	@InjectMocks
	private TaskController taskController;

	@Mock
	private TaskService taskService;

	@Test
	void contextLoads() {
	}

	@Test
	void testCreateTask() {
		Task newTask = new Task();
		newTask.setTaskid(1);
		newTask.setDescription("Test Task");
		newTask.setDueDate("2023-12-15");

		when(taskService.save(any(Task.class))).thenReturn(newTask);

		ResponseEntity<String> response = taskController.createTask(newTask);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("1", response.getBody());
	}

	@Test
	void testModifyTask() {
		Task modifiedTask = new Task();
		modifiedTask.setTaskid(1);
		modifiedTask.setDescription("Modified Description");

		when(taskService.modifyTask(any(Task.class))).thenReturn(true);

		ResponseEntity<String> response = taskController.modifyTask(modifiedTask);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Task modified successfully.", response.getBody());
	}

	@Test
	void testGetAllTasks() {
		List<Task> tasks = new ArrayList<>();
		Task task1 = new Task();
		task1.setTaskid(1);
		task1.setDescription("Task 1");

		Task task2 = new Task();
		task2.setTaskid(2);
		task2.setDescription("Task 2");

		tasks.add(task1);
		tasks.add(task2);

		when(taskService.getAllTasks()).thenReturn(tasks);

		ResponseEntity<List<Task>> response = taskController.getAllTasks();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}
}
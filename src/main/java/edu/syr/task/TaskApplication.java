package edu.syr.task;

import edu.syr.task.dto.TaskDTO;
import edu.syr.task.model.User;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}


	@Bean
	public CommandLineRunner run() {
		return args -> {
			RestTemplate restTemplate = new RestTemplate();

			// Create a User
			HttpHeaders createHeaders = new HttpHeaders();
			createHeaders.setContentType(MediaType.APPLICATION_JSON);
			String createUserJson = "{\"name\":\"Jack\",\"department\":\"HR\"}";
			HttpEntity<String> createUserEntity = new HttpEntity<>(createUserJson, createHeaders);
			ResponseEntity<User> createUserResponse = restTemplate.postForEntity("http://localhost:8080/users/create", createUserEntity, User.class);
			System.out.println("Create User Response: " + createUserResponse.getBody());
			ObjectId objectid = createUserResponse.getBody().getId();

			//Create a Task
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			String createJson = "{\"state\":\"TODO\",\"description\":\"Create a Backend for tasks\", \"dueDate\":\"2023-12-15\"}";
			HttpEntity<String> createEntity = new HttpEntity<>(createJson, headers);
			ResponseEntity<String> createResponse = restTemplate.exchange("http://localhost:8080/tasks/create", HttpMethod.POST, createEntity, String.class);
			System.out.println("Create Task Response: " + createResponse.getBody());


			//Modify the task
			Integer taskId = Integer.valueOf(createResponse.getBody());
			String modifyJson = "{\"state\":\"DOING\",\"description\":\"Create a Backend for tasks\",\"dueDate\":\"2023-12-15\",\"taskid\":" + taskId + ", \"assignedTo\":\"Jack\"}";
			HttpEntity<String> modifyEntity = new HttpEntity<>(modifyJson, headers);
			ResponseEntity<String> modifyResponse = restTemplate.exchange("http://localhost:8080/tasks/modify", HttpMethod.PUT, modifyEntity, String.class);
			System.out.println("Modify Task Response: " + modifyResponse.getBody());


			// Get All Users
			ResponseEntity<User[]> allUsersResponse = restTemplate.getForEntity("http://localhost:8080/users/getAllUsers", User[].class);
			User[] allUsers = allUsersResponse.getBody();

			if (allUsers != null) {
				for (User user : allUsers) {
					System.out.println("Name: " + user.getName());
					System.out.println("Department: " + user.getDepartment());

					List<TaskDTO> tasks = user.getTasks();
					if (tasks != null && !tasks.isEmpty()) {
						System.out.println("Tasks associated with this user:");
						for (TaskDTO task : tasks) {
							System.out.println("  Task ID: " + task.getTaskid());
							System.out.println("  Task State: " + task.getState());
						}
					} else {
						System.out.println("No tasks associated with this user.");
					}
					System.out.println("-----------------------");
				}
			} else {
				System.out.println("No users found.");
			}

			//Notify Users the task has been completed
			taskId = Integer.valueOf(createResponse.getBody());
			modifyJson = "{\"state\":\"DONE\",\"taskid\":" + taskId + "}";
			HttpEntity<String> modifyNotify = new HttpEntity<>(modifyJson, headers);
			ResponseEntity<String> modifyResponseNotify = restTemplate.exchange("http://localhost:8080/tasks/modify", HttpMethod.PUT, modifyNotify, String.class);
			System.out.println("Modify Task Response: " + modifyResponseNotify.getBody());

			//Get User by Task id
			ResponseEntity<Object> usersByTaskIdResponse = restTemplate.getForEntity("http://localhost:8080/users/taskid/{taskId}", Object.class, taskId);
			System.out.println("Users by Task ID: " + usersByTaskIdResponse.getBody());

			//Get All tasks (Show board)
			ResponseEntity<String> getAllResponse = restTemplate.getForEntity("http://localhost:8080/tasks/getAllTasks", String.class);
			System.out.println("Get All Tasks Response: " + getAllResponse.getBody());



			//Delete taskid
			restTemplate.delete("http://localhost:8080/tasks/delete/" + taskId);
			System.out.println("Delete Task Response: Task " + taskId + " deleted.");

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> deleteResponse = restTemplate.exchange(
					"http://localhost:8080/users/delete/" + objectid.toString(),
					HttpMethod.DELETE,
					entity,
					String.class);

			System.out.println("Delete Response: " + deleteResponse.getBody());
		};
	}


}

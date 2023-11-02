package edu.syr.task;

import edu.syr.task.model.User;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


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
			String createUserJson = "{\"name\":\"MIKE\",\"department\":\"IT\"}";
			HttpEntity<String> createUserEntity = new HttpEntity<>(createUserJson, createHeaders);
			ResponseEntity<User> createUserResponse = restTemplate.postForEntity("http://localhost:8080/users/create", createUserEntity, User.class);
			System.out.println("-----------------------------------------------------");
			System.out.println("Create User Response:");
			System.out.println("  " + createUserResponse.getBody());
			String objectid = createUserResponse.getBody().getId();
			System.out.println("-----------------------------------------------------");

			// Create a Task
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			String createJson = "{\"state\":\"TODO\",\"description\":\"Create a Backend for tasks\", \"dueDate\":\"2023-12-15\"}";
			HttpEntity<String> createEntity = new HttpEntity<>(createJson, headers);
			ResponseEntity<String> createResponse = restTemplate.exchange("http://localhost:8080/tasks/create", HttpMethod.POST, createEntity, String.class);
			System.out.println("Create Task Response:");
			System.out.println("  " + createResponse.getBody());
			System.out.println("-----------------------------------------------------");

			// Modify the task
			Integer taskId = Integer.valueOf(createResponse.getBody());
			String modifyJson = "{\"state\":\"DOING\",\"description\":\"Create a Backend for tasks\",\"dueDate\":\"2023-12-15\",\"taskid\":" + taskId + ", \"assignedTo\":\"Mike\"}";
			HttpEntity<String> modifyEntity = new HttpEntity<>(modifyJson, headers);
			ResponseEntity<String> modifyResponse = restTemplate.exchange("http://localhost:8080/tasks/modify", HttpMethod.PUT, modifyEntity, String.class);
			System.out.println("Modify Task Response:");
			System.out.println("  " + modifyResponse.getBody());
			System.out.println("-----------------------------------------------------");

			// Get All Users
			ResponseEntity<User[]> allUsersResponse = restTemplate.getForEntity("http://localhost:8080/users/getAllUsers", User[].class);
			User[] allUsers = allUsersResponse.getBody();
			System.out.println("All Users:");
			if (allUsers != null) {
				for (User user : allUsers) {
					System.out.println("  Name: " + user.getName());
					System.out.println("  Department: " + user.getDepartment());
					// ... (tasks associated with this user)
				}
			} else {
				System.out.println("  No users found.");
			}
			System.out.println("-----------------------------------------------------");

			// Notify Users the task has been completed
			taskId = Integer.valueOf(createResponse.getBody());
			modifyJson = "{\"state\":\"DONE\",\"taskid\":" + taskId + "}";
			HttpEntity<String> modifyNotify = new HttpEntity<>(modifyJson, headers);
			ResponseEntity<String> modifyResponseNotify = restTemplate.exchange("http://localhost:8080/tasks/modify", HttpMethod.PUT, modifyNotify, String.class);
			System.out.println("Modify Task Response:");
			System.out.println("  " + modifyResponseNotify.getBody());
			System.out.println("-----------------------------------------------------");

			// Get User by Task id
			ResponseEntity<Object> usersByTaskIdResponse = restTemplate.getForEntity("http://localhost:8080/users/taskid/{taskId}", Object.class, taskId);
			System.out.println("Users by Task ID:");
			System.out.println("  " + usersByTaskIdResponse.getBody());
			System.out.println("-----------------------------------------------------");

			// Get All tasks (Show board)
			ResponseEntity<String> getAllResponse = restTemplate.getForEntity("http://localhost:8080/tasks/getAllTasks", String.class);
			System.out.println("Get All Tasks Response:");
			System.out.println("  " + getAllResponse.getBody());
			System.out.println("-----------------------------------------------------");

			// Delete taskid
			restTemplate.delete("http://localhost:8080/tasks/delete/" + taskId);
			System.out.println("Delete Task Response:");
			System.out.println("  Task " + taskId + " deleted.");
			System.out.println("-----------------------------------------------------");

			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> deleteResponse = restTemplate.exchange("http://localhost:8080/users/delete/" + objectid, HttpMethod.DELETE, entity, String.class);
			System.out.println("Delete Response:");
			System.out.println("  " + deleteResponse.getBody());
			System.out.println("-----------------------------------------------------");
		};
	}

}

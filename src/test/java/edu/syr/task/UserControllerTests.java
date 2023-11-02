package edu.syr.task;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.syr.task.controller.UserController;
import edu.syr.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {

        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("name", "Dummy");
        userAttributes.put("department", "IT");

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAttributes)))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowUsers() throws Exception {
        mockMvc.perform(get("/users/getAllUsers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindUsersByTaskId() throws Exception {
        int taskId = 23;
        mockMvc.perform(get("/users/taskid/" + taskId))
                .andExpect(status().isOk());
    }

}
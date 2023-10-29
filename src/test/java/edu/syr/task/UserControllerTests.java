package edu.syr.task;


import edu.syr.task.controller.UserController;
import edu.syr.task.model.User;
import edu.syr.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        // Sample User to be returned by the mock service
        User mockUser = new User();
        mockUser.setId(null);
        mockUser.setName("John");
        mockUser.setDepartment("IT");

        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        String userJson = "{\"name\":\"John\",\"department\":\"IT\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());


    }
}
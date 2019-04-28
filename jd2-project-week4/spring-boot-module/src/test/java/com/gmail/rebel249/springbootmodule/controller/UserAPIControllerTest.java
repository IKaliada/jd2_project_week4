package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.repositorymodule.model.Role;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import com.gmail.rebel249.springbootmodule.config.ApiSecurityConfigurer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPIControllerTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private UsersAPIController userAPIController;
    @MockBean
    private ApiSecurityConfigurer apiSecurityConfigurer;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldSuccessfullyReturnStatus200() throws Exception {
        this.mockMvc.perform(get("/api/private/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAll() throws Exception {
        UserDTO userDTO = new UserDTO();
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        userDTO.setId(1L);
        userDTO.setUsername("username");
        userDTO.setPassword("123456");
        userDTO.setRole(role);
        List<UserDTO> users = Collections.singletonList(userDTO);
        ResponseEntity<List<UserDTO>> listResponseEntity = new ResponseEntity<>(users, HttpStatus.OK);

        given(userAPIController.getUsers()).willReturn(listResponseEntity);

        this.mockMvc.perform(get("/api/private/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"username\": \"username\", \"password\": \"123456\", \"role\": {\"id\": 1, \"name\": \"ADMIN\"}"));
    }
}

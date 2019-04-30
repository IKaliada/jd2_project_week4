package com.gmail.rebel249.springbootmodule.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ItemController itemController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldPassIfControllerIsNotNull() {
        assertThat(itemController).isNotNull();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void shouldSuccessfullyReturnStatus200() throws Exception {
        this.mockMvc.perform(get("/private/items"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldSuccessfullyReturnStatus200ForItemPage() throws Exception {
        this.mockMvc.perform(get("/private/items"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectLogin() throws Exception {
        this.mockMvc.perform(formLogin().user("customer@customer").password("root"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/private/items"));
    }
}

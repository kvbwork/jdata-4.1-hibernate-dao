package ru.netology.hibernatedao.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecuredController.class)
class SecuredControllerTest {
    private static final String ROOT_PATH = "/secured";

    @Autowired
    MockMvc mockMvc;

    @Test
    void read_anonymous_failure() throws Exception {
        String url = ROOT_PATH + "/read";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "READ")
    void read_role_read_success() throws Exception {
        String url = ROOT_PATH + "/read";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    void write_anonymous_failure() throws Exception {
        String url = ROOT_PATH + "/write";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "WRITE")
    void write_role_write_success() throws Exception {
        String url = ROOT_PATH + "/write";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    void delete_anonymous_failure() throws Exception {
        String url = ROOT_PATH + "/delete";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "WRITE")
    void delete_role_write_success() throws Exception {
        String url = ROOT_PATH + "/delete";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "DELETE")
    void delete_role_delete_success() throws Exception {
        String url = ROOT_PATH + "/delete";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    void user_anonymous_failure() throws Exception {
        String url = ROOT_PATH + "/user";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser("user")
    void user_has_same_name_success() throws Exception {
        String url = ROOT_PATH + "/user?username=user";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("user")
    void user_has_different_name_failure() throws Exception {
        String url = ROOT_PATH + "/user?username=manager";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isForbidden());
    }

}
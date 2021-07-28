package de.teamshrug.scooterms.controller;

import com.jayway.jsonpath.JsonPath;
import de.teamshrug.scooterms.SpringBootJwtApplication;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.repository.UserRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = "spring.profiles.active = test",
        classes = SpringBootJwtApplication.class)
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() throws Exception {
        List<UserDao> userlist = userRepository.findAll();
        for(UserDao user : userlist) {
            userRepository.delete(user);
        }
    }

    @Test
    public void UserRegisterTest() throws Exception {
        JSONObject register = new JSONObject();
        register.put("email", "testmail@gmail.com");
        register.put("password", "test");
        mvc.perform(post("/register")
                .content(String.valueOf(register))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    public void UserRegisterAndLoginTest() throws Exception {
        JSONObject register = new JSONObject();
        register.put("email", "testmail@gmail.com");
        register.put("password", "test");
        mvc.perform(post("/register")
                .content(String.valueOf(register))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        JSONObject login = new JSONObject();
        login.put("email", "testmail@gmail.com");
        login.put("password", "test");
        mvc.perform(post("/authenticate")
                .content(String.valueOf(login))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void UserRegisterAndLoginAndDeleteTest() throws Exception {
        JSONObject register = new JSONObject();
        register.put("email", "testmail@gmail.com");
        register.put("password", "test");
        mvc.perform(post("/register")
                .content(String.valueOf(register))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        JSONObject login = new JSONObject();
        login.put("email", "testmail@gmail.com");
        login.put("password", "test");
        MvcResult result = mvc.perform(post("/authenticate")
                .content(String.valueOf(login))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

        mvc.perform(delete("/accountmgr/deleteaccount")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(200));
    }

    @Test
    public void UserRegisterAndLoginAndTopUp() throws Exception {
        JSONObject register = new JSONObject();
        register.put("email", "testmail@gmail.com");
        register.put("password", "test");
        mvc.perform(post("/register")
                .content(String.valueOf(register))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        JSONObject login = new JSONObject();
        login.put("email", "testmail@gmail.com");
        login.put("password", "test");
        MvcResult result = mvc.perform(post("/authenticate")
                .content(String.valueOf(login))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

        mvc.perform(get("/accountmgr/myaccount/topup/{amount}", 100)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(200));
    }
}
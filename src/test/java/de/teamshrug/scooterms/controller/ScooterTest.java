package de.teamshrug.scooterms.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import de.teamshrug.scooterms.SpringBootJwtApplication;
import de.teamshrug.scooterms.model.Area;
import de.teamshrug.scooterms.model.MaintenanceDepartment;
import de.teamshrug.scooterms.model.Scooter;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.repository.AreaRepository;
import de.teamshrug.scooterms.repository.MaintenanceDepartmentRepository;
import de.teamshrug.scooterms.repository.ScooterRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = "spring.profiles.active = test",
        classes = SpringBootJwtApplication.class)
@AutoConfigureMockMvc
public class ScooterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ScooterRepository scooterRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaintenanceDepartmentRepository maintenanceDepartmentRepository;

    @Before
    public void setUp() throws Exception {
        Area TestCity = new Area("TestCity", new BigDecimal("50.95"), new BigDecimal("51.01"), new BigDecimal("11.00"), new BigDecimal("11.06"));
        areaRepository.save(TestCity);

        MaintenanceDepartment md = new MaintenanceDepartment("TestMD", 5, 0, new BigDecimal("50.994664"), new BigDecimal("11.042976"), TestCity);
        maintenanceDepartmentRepository.save(md);

        Scooter scooter = new Scooter(10, "lowonbattery", new BigDecimal("50.973841"), new BigDecimal("11.031959"), TestCity);
        scooterRepository.save(scooter);
        scooter = new Scooter(34, "damaged", new BigDecimal("50.970444"), new BigDecimal("11.038773"), TestCity);
        scooterRepository.save(scooter);
        scooter = new Scooter(58, "ready", new BigDecimal("50.987815"), new BigDecimal("11.027038"), TestCity);
        scooterRepository.save(scooter);

        JSONObject register = new JSONObject();
        register.put("email", "testmail@gmail.com");
        register.put("password", "test");
        mvc.perform(post("/register")
                .content(String.valueOf(register))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        // Make user admin to see scooters with every status
        if (userRepository.findById(1L).isPresent()) {
            UserDao user = userRepository.findById(1L).get();
            user.setAdmin(true);
            userRepository.save(user);
        }

    }

    @After
    public void tearDown() throws Exception {
        MaintenanceDepartment md = maintenanceDepartmentRepository.findByName("TestMD").get();
        Area area = areaRepository.findByName("TestCity").get();
        maintenanceDepartmentRepository.delete(md);
        areaRepository.delete(area);

        List<Scooter> scooterlist = scooterRepository.findAll();
        for(Scooter scooter : scooterlist) {
            scooterRepository.delete(scooter);
        }

        List<UserDao> userlist = userRepository.findAll();
        for(UserDao user : userlist) {
            userRepository.delete(user);
        }
    }

    @Test
    public void fetchReadyScooterTest() throws Exception {
        JSONObject login = new JSONObject();
        login.put("email", "testmail@gmail.com");
        login.put("password", "test");
        MvcResult loginresult = mvc.perform(post("/authenticate")
                .content(String.valueOf(login))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = JsonPath.read(loginresult.getResponse().getContentAsString(), "$.token");

        //Increase accounts credits by amount in €
        mvc.perform(get("/accountmgr/myaccount/topup/{amount}", 100)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(200));

        MvcResult result = mvc.perform(get("/scooters")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        String str = result.getResponse().getContentAsString();
        Type listOfScooter = new TypeToken<ArrayList<Scooter>>() {}.getType();
        Gson gson = new Gson();
        List<Scooter> scooterlist = gson.fromJson(str, listOfScooter);

        Scooter scooterToRent = null;

        for(Scooter scoo : scooterlist) {
            if (scoo.getStatus().equals("ready")) {
                System.out.println(scoo.getId());
                scooterToRent = scoo;
                break;
            }
        }

        assertThat(scooterToRent instanceof Scooter);
    }

    @Test
    public void RepairDamagedScooterAndReleaseItFromMaintenanceDepartmentTest() throws Exception {
        JSONObject login = new JSONObject();
        login.put("email", "testmail@gmail.com");
        login.put("password", "test");
        MvcResult loginresult = mvc.perform(post("/authenticate")
                        .content(String.valueOf(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String token = JsonPath.read(loginresult.getResponse().getContentAsString(), "$.token");

        // Retrieve all scooters
        MvcResult result = mvc.perform(get("/scooters")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        String str = result.getResponse().getContentAsString();
        Type listOfScooter = new TypeToken<ArrayList<Scooter>>() {}.getType();
        Gson gson = new Gson();
        List<Scooter> scooterlist = gson.fromJson(str, listOfScooter);

        Scooter damagedScooter = null;

        for(Scooter scoo : scooterlist) {
            if (scoo.getStatus().equals("damaged")) {
                System.out.println(scoo.getId());
                damagedScooter = scoo;
                break;
            }
        }

        // Bring one Scooter to Maintenancedepartment choosen by Scooter id
        mvc.perform(get("/scooters/repair/{id}", damagedScooter.getId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        // Release a random scooter from a MaintenanceDepartment choosen by MaintenanceDepartmert id
        mvc.perform(get("/maintenancedepartments/releasescooter/{mdid}", 1)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}

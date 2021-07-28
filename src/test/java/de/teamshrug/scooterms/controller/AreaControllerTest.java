package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.SpringBootJwtApplication;
import de.teamshrug.scooterms.model.Area;
import de.teamshrug.scooterms.repository.AreaRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = "spring.profiles.active = test",
        classes = SpringBootJwtApplication.class)
@AutoConfigureMockMvc
public class AreaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AreaRepository areaRepository;

    @Before
    public void setUp() throws Exception {
        Area TestCity = new Area("TestCity", new BigDecimal("50.95"), new BigDecimal("51.01"), new BigDecimal("11.00"), new BigDecimal("11.06"));
        areaRepository.save(TestCity);
    }

    @After
    public void tearDown() throws Exception {
        Area area = areaRepository.findByName("TestCity").get();
        areaRepository.delete(area);
    }

    @Test
    public void testFindByName() {
        Area found = areaRepository.findByName("TestCity").get();
        assertThat(found.getName())
                .isEqualTo("TestCity");
    }

    @Test
    public void checkCoordinateInArea() {
        Area found = areaRepository.findByName("TestCity").get();
        Assert.assertTrue(found.isInArea(new BigDecimal("50.99"), new BigDecimal("11.03")));
    }
}
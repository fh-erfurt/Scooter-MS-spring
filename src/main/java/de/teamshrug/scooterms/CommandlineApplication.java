package de.teamshrug.scooterms;

import de.teamshrug.scooterms.repository.*;
import de.teamshrug.scooterms.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class CommandlineApplication implements CommandLineRunner {

    private final AreaRepository areaRepository;
    private final MaintenanceDepartmentRepository maintenancedepartmentRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final ScooterHotspotRepository scooterhotspotRepository;

    public CommandlineApplication(AreaRepository areaRepository, MaintenanceDepartmentRepository maintenancedepartmentRepository, UserRepository userRepository, ScooterRepository scooterRepository, ScooterHotspotRepository scooterhotspotRepository) {
        this.areaRepository = areaRepository;
        this.maintenancedepartmentRepository = maintenancedepartmentRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
        this.scooterhotspotRepository = scooterhotspotRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CommandlineApplication.class, args);
    }

    @Override
    public void run(String... args) {

        if (scooterRepository.findAll().isEmpty() && maintenancedepartmentRepository.findAll().isEmpty() && scooterhotspotRepository.findAll().isEmpty() && userRepository.findAll().isEmpty() && areaRepository.findAll().isEmpty()) {
            System.out.println("Populating Database");

            Area erfurt = new Area("Erfurt", new BigDecimal("50.95"), new BigDecimal("51.01"), new BigDecimal("11.00"), new BigDecimal("11.06"));
            areaRepository.save(erfurt);
            erfurt = areaRepository.findAll().get(0);
            System.out.println(erfurt.getName());

            UserDao user1 = new UserDao("testmail1@gmail.com", "$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2", false, false);
            UserDao user2 = new UserDao("testmail2@gmail.com", "$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2", false, true);
            UserDao user3 = new UserDao("testmail3@gmail.com", "$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2", true, false);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            MaintenanceDepartment md = new MaintenanceDepartment("Leipziger Straße", 5, 0, new BigDecimal("50.994634"), new BigDecimal("11.042946"), erfurt);
            maintenancedepartmentRepository.save(md);

            ScooterHotspot sh = new ScooterHotspot("Hanseplatz", new BigDecimal("50.983801"), new BigDecimal("11.044834"), erfurt);
            scooterhotspotRepository.save(sh);

            Scooter scooter = new Scooter(99, "ready", new BigDecimal("50.974746"), new BigDecimal("11.0353385"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(10, "lowonbattery", new BigDecimal("50.973841"), new BigDecimal("11.031959"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(34, "damaged", new BigDecimal("50.970444"), new BigDecimal("11.038773"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(65, "damaged", new BigDecimal("50.973562"), new BigDecimal("11.052786"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(58, "ready", new BigDecimal("50.987815"), new BigDecimal("11.027038"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(37, "ready", new BigDecimal("50.990121"), new BigDecimal("11.012779"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(68, "ready", new BigDecimal("50.986996"), new BigDecimal("11.003527"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(15, "ready", new BigDecimal("50.974325"), new BigDecimal("11.014536"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(5, "lowonbattery", new BigDecimal("50.970868"), new BigDecimal("11.021255"), erfurt);
            scooterRepository.save(scooter);
            scooter = new Scooter(2, "lowonbattery", new BigDecimal("50.958063"), new BigDecimal("11.034833"), erfurt);
            scooterRepository.save(scooter);
        }
        else {
            System.out.println("Database not empty, using existing data");
        }
    }
}
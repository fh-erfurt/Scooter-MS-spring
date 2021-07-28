package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.MaintenanceDepartment;
import de.teamshrug.scooterms.model.Scooter;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.model.errors.MaintenanceDepartmentNotFoundException;
import de.teamshrug.scooterms.repository.MaintenanceDepartmentRepository;
import de.teamshrug.scooterms.repository.ScooterRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Transactional
@RestController
@RequestMapping("/maintenancedepartments")
public class MaintenanceDepartmentController {

    private final MaintenanceDepartmentRepository maintenancedepartmentRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDao getUserFromAuthorizationHeader(@NotNull String requestTokenHeader) {
        String jwtToken = requestTokenHeader.substring(7);
        String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findByEmail(extractedemail);
    }

    @Autowired
    public MaintenanceDepartmentController(MaintenanceDepartmentRepository maintenancedepartmentRepository, UserRepository userRepository, ScooterRepository scooterRepository) {
        this.maintenancedepartmentRepository = maintenancedepartmentRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
    }

    @GetMapping()
    ResponseEntity<List<MaintenanceDepartment>> findMaintenanceDepartments(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
        if (user.isAdmin())
        {
            return ResponseEntity.ok(this.maintenancedepartmentRepository.findAll());

        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<MaintenanceDepartment> findById(@PathVariable(value = "id") Long id) throws MaintenanceDepartmentNotFoundException {
        return ResponseEntity.ok(
                this.maintenancedepartmentRepository
                        .findById(id)
                        .orElseThrow(() -> new MaintenanceDepartmentNotFoundException("No Maintenancedepartment with this id: " + id))
        );
    }

    @GetMapping(path = "/releasescooter/{id}")
    ResponseEntity<String> releaseOneScooter(@PathVariable Long id, @RequestHeader(value="Authorization") String requestTokenHeader) throws MaintenanceDepartmentNotFoundException {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
        if (maintenancedepartmentRepository.existsById(id)) {
            try {
                MaintenanceDepartment department = this.maintenancedepartmentRepository.getById(id);
                List<Scooter> scooters = scooterRepository.findAll();
                Scooter scooter = null;

                for (Scooter scoo : scooters)  {
                    if (scoo.getNdegree().equals(department.getNdegree()) && scoo.getEdegree().equals(department.getEdegree())) {
                        department.setScootercapacity(department.getScootercapacity() - 1);
                        scooter = scoo;
                    }
                }

                if (scooter != null) {
                    BigDecimal newScooterNdegree = scooter.returnNearCoordinate(department.getNdegree());
                    BigDecimal newScooterEdegree = scooter.returnNearCoordinate(department.getEdegree());
                    scooter.setNdegree(newScooterNdegree);
                    scooter.setEdegree(newScooterEdegree);
                    scooter.setStatus("ready");

                    scooterRepository.save(scooter);
                    maintenancedepartmentRepository.save(department);

                    return new ResponseEntity<>(
                            HttpStatus.OK
                    );
                }

                return new ResponseEntity<>(
                        HttpStatus.BAD_REQUEST
                );
            }
            catch(Exception e)
            {
                return new ResponseEntity<>(
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
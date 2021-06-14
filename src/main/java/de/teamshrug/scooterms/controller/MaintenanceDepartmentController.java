package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.MaintenanceDepartment;
import de.teamshrug.scooterms.model.ScooterHotspot;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.model.errors.MaintenanceDepartmentNotFoundException;
import de.teamshrug.scooterms.model.errors.ScooterNotFoundException;
import de.teamshrug.scooterms.repository.MaintenanceDepartmentRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Transactional
@RestController
@RequestMapping("/maintenancedepartments")
public class MaintenanceDepartmentController {

    private final MaintenanceDepartmentRepository maintenancedepartmentRepository;
    private final UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDao getUserFromAuthorizationHeader(@NotNull String requestTokenHeader) {
        String jwtToken = requestTokenHeader.substring(7);
        String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findByEmail(extractedemail);
    }

    @Autowired
    public MaintenanceDepartmentController(MaintenanceDepartmentRepository maintenancedepartmentRepository, UserRepository userRepository) {
        this.maintenancedepartmentRepository = maintenancedepartmentRepository;
        this.userRepository = userRepository;
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
}
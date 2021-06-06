package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.model.MaintenanceDepartment;
import de.teamshrug.scooterms.model.errors.MaintenanceDepartmentNotFoundException;
import de.teamshrug.scooterms.repository.MaintenanceDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Transactional
@RestController
@RequestMapping("/maintenancedepartments")
public class MaintenanceDepartmentController {

    private final MaintenanceDepartmentRepository maintenancedepartmentRepository;

    @Autowired
    public MaintenanceDepartmentController(MaintenanceDepartmentRepository maintenancedepartmentRepository) {
        this.maintenancedepartmentRepository = maintenancedepartmentRepository;
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
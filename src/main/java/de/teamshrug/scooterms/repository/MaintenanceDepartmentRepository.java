package de.teamshrug.scooterms.repository;

import de.teamshrug.scooterms.model.MaintenanceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaintenanceDepartmentRepository extends JpaRepository<MaintenanceDepartment, Long> {

    public Optional<MaintenanceDepartment> findById(Long id);
}

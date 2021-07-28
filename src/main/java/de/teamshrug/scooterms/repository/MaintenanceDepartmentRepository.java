package de.teamshrug.scooterms.repository;

import de.teamshrug.scooterms.model.MaintenanceDepartment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaintenanceDepartmentRepository extends JpaRepository<MaintenanceDepartment, Long> {

    @NotNull
    public Optional<MaintenanceDepartment> findById(@NotNull Long id);

    public Optional<MaintenanceDepartment> findByName(String name);
}

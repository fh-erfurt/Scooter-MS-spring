package de.teamshrug.scooterms.repository;


import de.teamshrug.scooterms.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {

    public Optional<Area> findById(Long id);

    public Optional<Area> findByName(String name);
}

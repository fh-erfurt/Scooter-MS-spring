package de.teamshrug.scooterms.repository;


import de.teamshrug.scooterms.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    public Optional<Scooter> findById(Long id);

    List<Scooter> findAll();

    //public Optional<Scooter> findByArea(String area);
}

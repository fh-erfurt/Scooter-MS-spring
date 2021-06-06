package de.teamshrug.scooterms.repository;

import de.teamshrug.scooterms.model.ScooterHotspot;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScooterHotspotRepository extends JpaRepository<ScooterHotspot, Long> {

    @NotNull
    public Optional<ScooterHotspot> findById(@NotNull Long id);

    //public Optional<Scooter> findByAreaName(String areaname);
}


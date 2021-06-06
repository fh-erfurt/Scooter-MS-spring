package de.teamshrug.scooterms.repository;


import de.teamshrug.scooterms.model.Area;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {

    @NotNull
    public Optional<Area> findById(@NotNull Long id);

    public Optional<Area> findByName(String name);
}

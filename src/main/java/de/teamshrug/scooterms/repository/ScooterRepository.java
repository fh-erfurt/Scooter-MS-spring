package de.teamshrug.scooterms.repository;


import de.teamshrug.scooterms.model.RentalHistory;
import de.teamshrug.scooterms.model.Scooter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    @NotNull
    public Optional<Scooter> findById(@NotNull Long id);

    @NotNull
    List<Scooter> findAll();

    default List<Scooter> findAllReady() {
        List<Scooter> allScooters = findAll();

        allScooters.removeIf(scooter -> !scooter.getStatus().equals("ready"));
        return allScooters;
    };

    default List<Scooter> findAllReadyAndLowonbattery() {
        List<Scooter> allScooters = findAll();

        allScooters.removeIf(scooter -> (!scooter.getStatus().equals("ready")) && (!scooter.getStatus().equals("lowonbattery")));
        return allScooters;
    };

}

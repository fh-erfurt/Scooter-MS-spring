package de.teamshrug.scooterms.repository;

import de.teamshrug.scooterms.model.MaintenanceDepartment;
import de.teamshrug.scooterms.model.RentalHistory;
import de.teamshrug.scooterms.model.UserDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<RentalHistory, Long> {

    @NotNull
    public Optional<RentalHistory> findById(@NotNull Long id);

    public Optional<RentalHistory> findByUser(UserDao user);

    public List<RentalHistory> findAllByUser(UserDao user);

}
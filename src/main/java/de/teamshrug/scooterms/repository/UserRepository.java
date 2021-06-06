package de.teamshrug.scooterms.repository;
import de.teamshrug.scooterms.model.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserDao, Long> {
    UserDao findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

}
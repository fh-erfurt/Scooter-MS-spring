package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.RentalHistory;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.repository.RentalRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/accountmgr")
public class UserController  {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDao getUserFromAuthorizationHeader(@NotNull String requestTokenHeader) {
        String jwtToken = requestTokenHeader.substring(7);
        String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findByEmail(extractedemail);
    }

    @Autowired
    public UserController(UserRepository userRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping(path = "/myaccount")
    ResponseEntity<UserDao> showRentalHistory(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/myhistory")
    ResponseEntity<List<RentalHistory>> findByJWT(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);

        return ResponseEntity.ok(
                rentalRepository.findAllByUser(user)
        );
    }

    /*
    @GetMapping()
    ResponseEntity<Account> findByeMail(
            @RequestParam(value = "email") String email
            ,@RequestParam(value = "pw") String passwordhash
    ) throws AccountNotFoundException {
        return ResponseEntity.ok(
                this.userRepository
                        //.findByeMail(email)
                        .findByeMailAndPasswordHash(email, passwordhash)
                        .orElseThrow(() -> new AccountNotFoundException("Email or password not accepted."))
        );
    }*/

    /*@PostMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<Account> save(@RequestBody Account account) {
        return ResponseEntity.ok(this.userRepository.save(account));
    }*/

    @PutMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<UserDao> update(@RequestBody UserDao user) {
        return ResponseEntity.ok(this.userRepository.save(user));
    }

    /*@DeleteMapping("/{id}")
    void deleteAccount(@PathVariable(value = "id") Long id) {
        this.userRepository.deleteById(id);
    }*/

    /*@GetMapping()
    ResponseEntity<Account> findByeMail(@RequestParam(value = "email") String email) throws AccountNotFoundException {
        return ResponseEntity.ok(
                this.accountRepository
                        .findByeMail(email)
                        .orElseThrow(() -> new AccountNotFoundException("No Account with this email: " + email))
        );
    }*/

    @DeleteMapping(path = "/deleteaccount")
    void deleteAccount(@RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);

        try {
            this.userRepository.deleteByEmail(user.getEmail());
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}

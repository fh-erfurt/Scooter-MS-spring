package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@RestController
@RequestMapping("/accountmgr")
public class UserController  {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*
    @GetMapping(path = "/{id}")
    ResponseEntity<Account> findById(@PathVariable(value = "id") Long id) throws AccountNotFoundException {
        return ResponseEntity.ok(
                this.accountRepository
                        .findById(id)
                        .orElseThrow(() -> new AccountNotFoundException("No Account with this id: " + id))
        );
    }*/

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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @DeleteMapping()
    void deleteAccount(@RequestParam(value = "email") String email, @RequestHeader(value="Authorization") String requestTokenHeader) {

        //final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (extractedemail.equals(email)) {
                this.userRepository.deleteByEmail(email);
            }
            else {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED
                );
            }
        }
    }
}

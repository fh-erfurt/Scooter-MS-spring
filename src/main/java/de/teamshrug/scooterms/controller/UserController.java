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

import java.math.BigDecimal;
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
    ResponseEntity<UserDao> showAccountInfo(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
        return ResponseEntity.ok(user);
    }

    /**
     * top up user account
     * @param amount top-up amount in €
     * @param requestTokenHeader
     * @return
     */
    @GetMapping(path = "/myaccount/topup/{amount}")
    ResponseEntity<String> topUpBalance(@PathVariable int amount,@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
       UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
       BigDecimal credits = user.getCreditedEuros();
       credits = credits.add(BigDecimal.valueOf(amount));
       user.setCreditedEuros(credits);
       userRepository.save(user);
       return ResponseEntity.ok("Top Up Credits");
    }

    /**
     * returns scooter rental history
     * @param requestTokenHeader
     * @return
     */
    @GetMapping(path = "/myhistory")
    ResponseEntity<List<RentalHistory>> showRentalHistory(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);

        return ResponseEntity.ok(
                rentalRepository.findAllByUser(user)
        );
    }

    /**
     * delete account and rental history
     * @param requestTokenHeader
     */
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

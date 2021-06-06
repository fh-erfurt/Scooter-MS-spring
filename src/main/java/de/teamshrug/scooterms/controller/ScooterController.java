package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.PositionDto;
import de.teamshrug.scooterms.model.RentalHistory;
import de.teamshrug.scooterms.model.Scooter;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.model.errors.ScooterNotFoundException;
import de.teamshrug.scooterms.repository.RentalRepository;
import de.teamshrug.scooterms.repository.ScooterRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import de.teamshrug.scooterms.tools.Haversine;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/scooters")
public class ScooterController {

    private final ScooterRepository scooterRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDao getUserFromAuthorizationHeader(@NotNull String requestTokenHeader) {
        String jwtToken = requestTokenHeader.substring(7);
        String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findByEmail(extractedemail);
    }

    @Autowired
    public ScooterController(ScooterRepository scooterRepository, RentalRepository rentalRepository, UserRepository userRepository) {
        this.scooterRepository = scooterRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<Scooter> findById(@PathVariable(value = "id") Long id) throws ScooterNotFoundException {
        return ResponseEntity.ok(
                this.scooterRepository
                        .findById(id)
                        .orElseThrow(() -> new ScooterNotFoundException("No Scooter with this id: " + id))
        );
    }

    @GetMapping()
    ResponseEntity<List<Scooter>> findAll(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) throws ScooterNotFoundException {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);

        if (!user.isScooterHunter() && !user.isAdmin())
        {
            return ResponseEntity.ok(
                    this.scooterRepository
                            .findAllReady()
            );
        }
        if (user.isScooterHunter() && !user.isAdmin())
        {
            return ResponseEntity.ok(
                    this.scooterRepository
                            .findAllReadyAndLowonbattery()
            );
        }
        else {
            return ResponseEntity.ok(
                    this.scooterRepository
                            .findAll()
            );
        }
    }

    /*@GetMapping(path = "/{id}/rent")
    ResponseEntity<RentalHistory> save(@PathVariable Long id, @RequestHeader(value="Authorization") String requestTokenHeader) {
        if (scooterRepository.existsById(id) && scooterRepository.getById(id).getStatus().equals("ready")) {

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                String jwtToken = requestTokenHeader.substring(7);
                String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
                Long userid = userRepository.findByEmail(extractedemail).getId();
                Long timestamp = Instant.now().getEpochSecond();

                RentalHistory rentalentity = new RentalHistory(timestamp, timestamp, scooterRepository.getById(id), userRepository.getById(userid));

                this.scooterRepository.getById(id).setStatus("inuse");

                return ResponseEntity.ok(
                        this.rentalRepository
                                .save(rentalentity)
                );

            }
            else {
                return new ResponseEntity<>(
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }
    }*/

    @GetMapping(path = "/{id}/rent")
    ResponseEntity<Long> save(@PathVariable Long id, @RequestHeader(value="Authorization") String requestTokenHeader) {
        if (scooterRepository.existsById(id) && scooterRepository.getById(id).getStatus().equals("ready")) {
            try {
                UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
                long timestamp = Instant.now().getEpochSecond();

                RentalHistory rentalentity = new RentalHistory();
                rentalentity.setStart_timestamp(timestamp);
                rentalentity.setEnd_timestamp(timestamp);
                rentalentity.setUser(user);
                rentalentity.setScooter(scooterRepository.getById(id));

                List<RentalHistory> userhistory = rentalRepository.findAllByUser(user);

                boolean hasactiverental = false;
                for (RentalHistory rentalentry : userhistory) {
                    if (rentalentry.getStart_timestamp() == rentalentry.getEnd_timestamp()) {
                        hasactiverental = true;
                        break;
                    }
                }

                if (!hasactiverental) {
                    this.rentalRepository.save(rentalentity);
                    Scooter scooter = this.scooterRepository.getById(id);
                    scooter.setStatus("inuse");
                    scooterRepository.save(scooter);
                    return new ResponseEntity<>(
                            timestamp,
                            HttpStatus.OK
                    );
                }
                else {
                    return new ResponseEntity<>(
                            HttpStatus.NOT_ACCEPTABLE
                    );
                }
            }
            catch(Exception e)
            {
                return new ResponseEntity<>(
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    //@PathVariable Long id,
    @PostMapping(path = "/park")
    ResponseEntity<String> update(@RequestHeader(value="Authorization") String requestTokenHeader, @RequestBody PositionDto pos) {
        try {
            UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
            long timestamp = Instant.now().getEpochSecond();

            List<RentalHistory> rentallist = rentalRepository.findAllByUser(user);
            RentalHistory rentalentity = null;

            for (RentalHistory rentalentry : rentallist) {
                if (rentalentry.getStart_timestamp() == rentalentry.getEnd_timestamp()) {
                    rentalentity = rentalentry;
                    break;
                }
            }

            assert rentalentity != null;

            Scooter scooter = rentalentity.getScooter();

            if (rentalentity.getStart_timestamp() == rentalentity.getEnd_timestamp() && rentalentity.getScooter().equals(scooter) && scooter.isInRegisteredArea() )
            {
                rentalentity.setEnd_timestamp(timestamp);
                rentalRepository.save(rentalentity);

                double kmdriven = Haversine.distance(pos.getLatitude(), pos.getLongitude(), scooter.getNdegree().doubleValue(), scooter.getEdegree().doubleValue());
                double mdriven = kmdriven*1000;


                scooter.setBattery(scooter.getBattery() - (int)(mdriven*0.005));
                user.setCreditedEuros((user.getCreditedEuros().subtract(new BigDecimal(kmdriven))).setScale(2, RoundingMode.HALF_UP));

                scooter.setNdegree(BigDecimal.valueOf(pos.getLatitude()));
                scooter.setEdegree(BigDecimal.valueOf(pos.getLongitude()));

                if (scooter.getBattery() >= 20)
                {
                    scooter.setStatus("ready");
                }
                else
                {
                    scooter.setStatus("lowonbattery");
                }

                userRepository.save(user);
                scooterRepository.save(scooter);
                return new ResponseEntity<>(
                        HttpStatus.OK
                );

            }
            else {
                return new ResponseEntity<>(
                        HttpStatus.NOT_ACCEPTABLE
                );
            }

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /*@PostMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<Account> save(@RequestBody Account account) {
        return ResponseEntity.ok(this.accountRepository.save(account));
    }*/

    //.orElseThrow(() -> new ScooterNotFoundException("No Scooter with this id: " + id))
    /*
    @PostMapping(path = "{id}/rent")
    ResponseEntity<Scooter> findById(@PathVariable(value = "id") Long id) throws ScooterNotFoundException {
        return ResponseEntity.ok(
                this.scooterRepository
                        .findById(id)
                        .orElseThrow(() -> new ScooterNotFoundException("No Scooter with this id: " + id))
        );
    }*/
}
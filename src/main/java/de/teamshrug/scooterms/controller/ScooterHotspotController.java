package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.ScooterHotspot;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.model.errors.ScooterHotspotNotFoundException;
import de.teamshrug.scooterms.repository.ScooterHotspotRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/scooterhotspots")
public class ScooterHotspotController {

    private final ScooterHotspotRepository scooterhotspotRepository;
    private final UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDao getUserFromAuthorizationHeader(@NotNull String requestTokenHeader) {
        String jwtToken = requestTokenHeader.substring(7);
        String extractedemail = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userRepository.findByEmail(extractedemail);
    }

    @Autowired
    public ScooterHotspotController(ScooterHotspotRepository scooterhotspotRepository, UserRepository userRepository, UserRepository userRepository1) {
        this.scooterhotspotRepository = scooterhotspotRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    ResponseEntity<List<ScooterHotspot>> findScooterHotspots(@NotNull @RequestHeader(value="Authorization") String requestTokenHeader) {
        UserDao user = getUserFromAuthorizationHeader(requestTokenHeader);
        if(user.isScooterHunter() || user.isAdmin()) {
            return ResponseEntity.ok(this.scooterhotspotRepository.findAll());
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ScooterHotspot> findById(@PathVariable(value = "id") Long id) throws ScooterHotspotNotFoundException {
        return ResponseEntity.ok(
                this.scooterhotspotRepository
                        .findById(id)
                        .orElseThrow(() -> new ScooterHotspotNotFoundException("No Scooterhotspot with this id: " + id))
        );
    }
}
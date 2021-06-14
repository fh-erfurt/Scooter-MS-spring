package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.config.JwtTokenUtil;
import de.teamshrug.scooterms.model.ScooterHotspot;
import de.teamshrug.scooterms.model.UserDao;
import de.teamshrug.scooterms.model.errors.ScooterHotspotNotFoundException;
import de.teamshrug.scooterms.model.errors.ScooterNotFoundException;
import de.teamshrug.scooterms.repository.ScooterHotspotRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/scooterhotspots")
public class ScooterHotspotController {

    private final ScooterHotspotRepository scooterhotspotRepository;

    @Autowired
    public ScooterHotspotController(ScooterHotspotRepository scooterhotspotRepository, UserRepository userRepository) {
        this.scooterhotspotRepository = scooterhotspotRepository;
    }

    @GetMapping()
    ResponseEntity<List<ScooterHotspot>> findScooterHotspots() {
        return ResponseEntity.ok(this.scooterhotspotRepository.findAll());

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
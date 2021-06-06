package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.model.ScooterHotspot;
import de.teamshrug.scooterms.model.errors.ScooterHotspotNotFoundException;
import de.teamshrug.scooterms.repository.ScooterHotspotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/scooterhotspots")
public class ScooterHotspotController {

    private final ScooterHotspotRepository scooterhotspotRepository;

    @Autowired
    public ScooterHotspotController(ScooterHotspotRepository scooterhotspotRepository) {
        this.scooterhotspotRepository = scooterhotspotRepository;
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
package de.teamshrug.scooterms.controller;

import de.teamshrug.scooterms.model.Area;
import de.teamshrug.scooterms.model.errors.AreaNotFoundException;
import de.teamshrug.scooterms.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequestMapping("/areas")
public class AreaController {
    private final AreaRepository areaRepository;

    @Autowired
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<Area> findById(@PathVariable(value = "id") Long id) throws AreaNotFoundException {
        return ResponseEntity.ok(
                this.areaRepository
                        .findById(id)
                        .orElseThrow(() -> new AreaNotFoundException("No Area with this id: " + id))
        );
    }

    @GetMapping()
    ResponseEntity<Area> findByName(@RequestParam(value = "name") String name) throws AreaNotFoundException {
        return ResponseEntity.ok(
                this.areaRepository
                        .findByName(name)
                        .orElseThrow(() -> new AreaNotFoundException("No Area with this name: " + name))
        );
    }
}

package de.teamshrug.scooterms.model.errors;

import java.io.Serial;

public class ScooterHotspotNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -7366814517715710341L;

    public ScooterHotspotNotFoundException(String message) {
        super(message);
    }
}

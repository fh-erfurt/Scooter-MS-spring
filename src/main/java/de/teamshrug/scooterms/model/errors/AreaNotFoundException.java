package de.teamshrug.scooterms.model.errors;

import java.io.Serial;

public class AreaNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -7366814517715710341L;

    public AreaNotFoundException(String message) {
        super(message);
    }
}

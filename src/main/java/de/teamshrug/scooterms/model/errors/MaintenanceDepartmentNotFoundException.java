package de.teamshrug.scooterms.model.errors;

import java.io.Serial;

public class MaintenanceDepartmentNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -7366814517715710341L;

    public MaintenanceDepartmentNotFoundException(String message) {
        super(message);
    }
}

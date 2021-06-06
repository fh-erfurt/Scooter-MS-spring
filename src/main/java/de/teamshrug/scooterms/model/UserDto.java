package de.teamshrug.scooterms.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserDto {
    private  String email;
    private  String password;

    private  BigDecimal creditedEuros;
    private  boolean isAdmin;
    private  boolean isScooterHunter;
}

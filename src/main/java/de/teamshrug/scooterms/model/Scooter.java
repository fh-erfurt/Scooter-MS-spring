package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.teamshrug.scooterms.repository.ScooterRepository;
import de.teamshrug.scooterms.repository.UserRepository;
import de.teamshrug.scooterms.tools.Haversine;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Random;

/**
 * The scooter class can be used by a customer as a vehicle until the battery is empty, the customer pays a fixed price for every minute of travel. When the battery is below 20 percent, its status changes to "lowonbattery" and can now be picked up by a scooter hunter to charge it, the scooter hunter receives a reward for his effort.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    private  int battery;

    private  String licenseplate;

    private  String status;

    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree;

    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    private Area area;

    public Scooter(int battery, String status, BigDecimal ndegree, BigDecimal edegree, Area area) {
        this.battery = battery;
        this.licenseplate = requestLicensePlate();
        this.status = status;
        this.ndegree = ndegree;
        this.edegree = edegree;
        this.area = area;
    }

    /**
     * generates a license plate for the scooter that calls the method like '263ZDE'
     */
    String requestLicensePlate()
    {
        return genRandomNumber(100,999) + genChars();
    }

    /**
     * generates a random number between min and max value
     * @param min is the lowest value
     * @param max is the highest value
     * @return random number
     */
    private int genRandomNumber(int min, int max)
    {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * generates random strings for the license plate
     * @return random strings
     */
    public String genChars()
    {
        char one   = (char)(Math.random()*26 + 'A');
        char two   = (char)(Math.random()*26 + 'A');
        char three = (char)(Math.random()*26 + 'A');
        return String.valueOf(one) + String.valueOf(two) + String.valueOf(three);
    }


    /**
     * Calculates the driven distance and deducts the costs from the user and reduces the battery charge (each KM started 1€)
     * If the battery drops under 20% when the scooter is parked, it state changes to lowonbattery and a scooterhunter is able to pick it up for recharging
     */


    /**
     * @return checks if current position is in the registered Area
     */
    public boolean isInRegisteredArea()
    {
        return area.isInArea(new BigDecimal(String.valueOf(getNdegree())), new BigDecimal(String.valueOf(getEdegree())));
    }

    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal returnNearCoordinate(BigDecimal input) {
        BigDecimal adder = generateRandomBigDecimalFromRange( new BigDecimal("-0.001"), new BigDecimal("0.001"));
        return adder.add(input);
    }

}

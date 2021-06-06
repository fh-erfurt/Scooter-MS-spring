package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

/**
 *
 * @author Roman Raßloff
 * @version 0.0.0.0, 05/02/2021
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentalhistory")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RentalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    private  long start_timestamp;

    private  long end_timestamp;


    @ManyToOne(cascade = CascadeType.ALL)
    private Scooter scooter;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDao user;

}

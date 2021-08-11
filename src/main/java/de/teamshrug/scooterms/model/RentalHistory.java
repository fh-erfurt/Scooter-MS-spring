package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Storing rentals with fk to scooter and user + start/end -timestamp of the rental
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

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Scooter scooter;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserDao user;

    public RentalHistory(long timestamp, long timestamp1, Scooter scoo, UserDao userdao) {
        start_timestamp = timestamp;
        end_timestamp = timestamp1;
        scooter = scoo;
        user = userdao;
    }
}

package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

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
@Table(name = "scooterhotspot")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ScooterHotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(unique = true, nullable = false)
    private  String name;

    private  int maxscootercount;
    private  int scootercount;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree;


    @ManyToOne(cascade = CascadeType.ALL)
    private Area area;
}

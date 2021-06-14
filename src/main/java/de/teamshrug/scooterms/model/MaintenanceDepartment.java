package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author Roman Raßloff, Jonas Waldhelm
 * @version 0.0.0.0, 14/06/2021
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maintenancedepartment")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MaintenanceDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private  String name;

    private  int maxscootercapacity;
    private  int scootercapacity;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Area area;

}
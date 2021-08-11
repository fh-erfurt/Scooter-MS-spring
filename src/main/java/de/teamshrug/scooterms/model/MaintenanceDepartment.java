package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * When a scooter has to be repaired, it disappears from the map and its location is set to the MaintenanceDepartment
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
    @ManyToOne(cascade = CascadeType.MERGE)
    private Area area;

    public MaintenanceDepartment(String name, int maxscootercapacity, int scootercapacity, @NotNull BigDecimal ndegree, @NotNull BigDecimal edegree, Area area) {
        this.name = name;
        this.maxscootercapacity = maxscootercapacity;
        this.scootercapacity = scootercapacity;
        this.ndegree = ndegree;
        this.edegree = edegree;
        this.area = area;
    }
}
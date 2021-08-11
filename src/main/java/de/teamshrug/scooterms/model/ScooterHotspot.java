package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * When a scooter is charged, it appears at a ScooterHotspot
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

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private Area area;

    public ScooterHotspot(String name, @NotNull BigDecimal ndegree, @NotNull BigDecimal edegree, Area area) {
        this.name = name;
        this.ndegree = ndegree;
        this.edegree = edegree;
        this.area = area;
    }
}

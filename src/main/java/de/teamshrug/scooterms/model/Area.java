package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(unique = true, nullable = false)
    private  String name;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree1;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal ndegree2;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree1;

    @NotNull
    @Column(columnDefinition = "decimal(8,6)")
    private  BigDecimal edegree2;


    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private Set<MaintenanceDepartment> maintenancedepartmentlist = new HashSet<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private Set<ScooterHotspot> scooterhotspotlist = new HashSet<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private Set<Scooter> scooterlist = new HashSet<>();


    /**
     * checks whether the transferred coordinates are in the area
     * @param ndegree - the current location
     * @param edegree - the current location
     */
    public boolean isInArea(BigDecimal ndegree, BigDecimal edegree)
    {
        //ndegree1 < ndegree && (ndegree < ndegree2)) || ((ndegree1 > ndegree) && (ndegree > ndegree2)
        if ((((ndegree.compareTo(ndegree1)) > 0) && ((ndegree2.compareTo(ndegree)) > 0)) || (((ndegree1.compareTo(ndegree)) > 0) && ((ndegree.compareTo(ndegree2)) > 0))) {
            // ((edegree1 < _position.edegree) && (_position.edegree < edegree2)) || ((edegree1 > _position.edegree) && (_position.edegree > edegree2)) {
            if ((((edegree.compareTo(edegree1)) > 0) && (edegree2.compareTo(edegree)) > 0) || (((edegree1.compareTo(edegree)) > 0) && (edegree.compareTo(edegree2)) > 0)) {
                return true;
            }
            else
                return false;
        }
        else
            return false;

    }
}
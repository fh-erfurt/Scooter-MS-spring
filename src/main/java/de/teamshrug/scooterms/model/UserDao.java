package de.teamshrug.scooterms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.teamshrug.scooterms.repository.ScooterHotspotRepository;
import de.teamshrug.scooterms.tools.Haversine;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(unique = true, nullable = false)
    private  String email;

    @Column
    @JsonIgnore
    private  String password;

    private  BigDecimal creditedEuros;

    private  boolean isAdmin;

    private  boolean isScooterHunter;

    public ScooterHotspot returnNearestScooterHotspot(Scooter scooter, List<ScooterHotspot> scooterhotspotlist) {
        ScooterHotspot nearestScooterHotspot = null;
        double distance = Double.MAX_VALUE;

        for ( ScooterHotspot hotspot : scooterhotspotlist) {
            double calcdistance = Haversine.distance(scooter.getNdegree().doubleValue(), scooter.getEdegree().doubleValue(), hotspot.getNdegree().doubleValue(), hotspot.getEdegree().doubleValue());
            if (calcdistance < distance) {
                nearestScooterHotspot = hotspot;
            }
        }

        return nearestScooterHotspot;
    }

    public MaintenanceDepartment returnNearestMaintenanceDepartment(Scooter scooter, List<MaintenanceDepartment> maintenanceDepartmentList) {
        MaintenanceDepartment nearestMaintenanceDepartment = null;
        double distance = Double.MAX_VALUE;

        for ( MaintenanceDepartment department : maintenanceDepartmentList) {
            double calcdistance = Haversine.distance(scooter.getNdegree().doubleValue(), scooter.getEdegree().doubleValue(), department.getNdegree().doubleValue(), department.getEdegree().doubleValue());
            if (calcdistance < distance) {
                nearestMaintenanceDepartment = department;
            }
        }

        return nearestMaintenanceDepartment;
    }
}


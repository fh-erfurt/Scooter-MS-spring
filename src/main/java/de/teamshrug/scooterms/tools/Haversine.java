package de.teamshrug.scooterms.tools;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Haversine /*implements Serializable*/ {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    /*public int meterdriven(double ndestination, double edestination) {
        float kmdriven = (float) distance(ndegree.doubleValue(),edegree.doubleValue(),ndestination,edestination);

        float roundkmdistance = (float)(Math.round(kmdriven * 100.0) / 100.0);      // like this 4.11  rounds kmdriven to 2 decimal places
        return (int)(kmdriven*1000);
    }*/

}
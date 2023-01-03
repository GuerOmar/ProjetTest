package org.example;

import java.util.ArrayList;
import java.util.List;

public class TCAS {

    // La distance verticale en pied pour considérer un danger
    private static double CONFLICT_ALTITUDE_THRESHOLD = 0.750 ;
    // La distance horizontale en mile marin pour considérer un danger
    private static int CONFLICT_DISTANCE_THRESHOLD = 5 ;
    // L'avion où le systeme TCAS est installé
    private Aircraft myAircraft;
    // Liste des avions dans le système TCAS
    private List<Aircraft> aircrafts = new ArrayList<>();

    public TCAS(Aircraft aircraft){
        myAircraft = aircraft;
    }

    // Méthode pour ajouter un avion à la liste
    public void addAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
    }

    // Méthode pour détecter les conflits entre avions
    public void detectConflicts() {
        for (int i = 0; i < aircrafts.size(); i++) {
            Aircraft aircraft = aircrafts.get(i);
                if (isConflict(aircraft)) {
                    // Gérer le conflit entre les deux avions
                    resolveConflict(myAircraft, aircraft);
                }
        }
    }


    // Méthode pour vérifier si deux avions sont en conflit
    public boolean isConflict(Aircraft aircraft) {
        // Récupérer les données de position et de mouvement des deux avions
        double lat1 = myAircraft.getLatitude();
        double lon1 = myAircraft.getLongitude();
        double alt1 = myAircraft.getAltitude();
        double lat2 = aircraft.getLatitude();
        double lon2 = aircraft.getLongitude();
        double alt2 = aircraft.getAltitude();

        // Calculer la distance et l'altitude relative entre les deux avions
        double distance = calcDistance(lat1, lon1, lat2, lon2);
        double relativeAltitude = calcRelativeAltitude(alt1, alt2);

        System.out.println("distance " + distance);
        System.out.println("altitude " + relativeAltitude);

        // Vérifier si la distance et l'altitude relative entre les deux avions
        // dépassent les seuils prédéfinis
        if (distance < CONFLICT_DISTANCE_THRESHOLD && relativeAltitude < CONFLICT_ALTITUDE_THRESHOLD) {
            // Les deux avions sont en conflit
            return true;
        } else {
            // Les deux avions ne sont pas en conflit
            return false;
        }
    }
    // Méthode pour résoudre un conflit entre deux avions
    private void resolveConflict(Aircraft aircraft1, Aircraft aircraft2) {
        // Envoyer des instructions de résolution de conflit aux deux avions
        aircraft1.resolveConflict();
        aircraft2.resolveConflict();
    }

    // Méthode pour calculer la distance entre deux avions
    public double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371.0; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                   Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c; // Distance in kilometers

        // Convert distance from kilometers to miles
        double distanceInMiles = distance / 1.609;

        return distanceInMiles;
    }


    // Méthode pour calculer l'altitude relative entre deux avions
    public double calcRelativeAltitude(double alt1, double alt2) {
        return Math.abs(alt1 - alt2);
    }


}

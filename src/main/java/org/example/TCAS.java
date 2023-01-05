package org.example;

import java.util.ArrayList;
import java.util.List;


public class TCAS {


    // La distance verticale en pied pour considérer un danger
    private static int CONFLICT_RELATIVE_ALTITUDE = 750 ;
    // La distance horizontale en mile marin pour considérer un danger
    private static int CONFLICT_DISTANCE = 5 ;
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
    public boolean detectConflicts() {
        for (int i = 0; i < aircrafts.size(); i++) {
            Aircraft aircraft = aircrafts.get(i);
                if (isConflict(aircraft)) {
                    // Conflit détecter
                    return true;
                }
        }
        return false;
    }


    // Méthode pour vérifier si deux avions sont en conflit
    public boolean isConflict(Aircraft aircraft) {
        // Récupérer les données de position et de mouvement des deux avions
        double lat1 = myAircraft.getLatitude();
        double lon1 = myAircraft.getLongitude();
        int alt1 = myAircraft.getAltitude();
        double lat2 = aircraft.getLatitude();
        double lon2 = aircraft.getLongitude();
        int alt2 = aircraft.getAltitude();

        // Calculer la distance et l'altitude relative entre les deux avions
        double distance = calcDistance(lat1, lon1, lat2, lon2);
        double relativeAltitude = calcRelativeAltitude(alt1, alt2);

        // Vérifier si la distance et l'altitude relative entre les deux avions
        // dépassent les seuils prédéfinis
        if (distance < CONFLICT_DISTANCE && relativeAltitude < CONFLICT_RELATIVE_ALTITUDE) {
            // Les deux avions sont en conflit
            return true;
        } else {
            // Les deux avions ne sont pas en conflit
            return false;
        }
    }

    // Méthode pour calculer la distance entre deux avions
    public double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        if(lat1 > 90.0 || lat1<-90.0)
            throw new IllegalStateException("La valeur de la latitude 1 doit etre comprise entre -90 et 90");
        if(lat2 >90.0 || lat2<-90.0)
            throw new IllegalStateException("La valeur de la latitude 2 doit etre comprise entre -90 et 90");
        if(lat2 >90.0 || lat2<-90.0)
            throw new IllegalStateException("La valeur de la latitude 2 doit etre comprise entre -90 et 90");
        if(lon1 >180.0 || lon1<-180.0)
            throw new IllegalStateException("La valeur de la longitude 1 doit etre comprise entre -180 et 180");
        if(lon2 >180.0 || lon2<-180.0)
            throw new IllegalStateException("La valeur de la longitude 2 doit etre comprise entre -180 et 180");

        double earthRadius = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                   Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c; // Distance en kilometers

        // Convertir la distance de kilometers à miles
        double distanceInMiles = distance / 1.609;

        return distanceInMiles;
    }


    // Méthode pour calculer l'altitude relative entre deux avions
    public double calcRelativeAltitude(int alt1, int alt2) {
        if( alt1 < 0)
            throw new IllegalStateException("La valeur de l'altitude 1 doit etre positive");
        if( alt2 < 0)
            throw new IllegalStateException("La valeur de l'altitude 2 doit etre positive");
        return Math.abs(alt1 - alt2);
    }


}

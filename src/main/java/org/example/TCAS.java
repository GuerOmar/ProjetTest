package org.example;

import java.util.ArrayList;
import java.util.List;

/*
   HLR : La fonction doit prendre en entrée 2 entier  et retourner leur plus grand multiple commun
   LLR1 : Si l'un des entrée est nulle, la fonction retourne la valeur 0
   LLR2 : Si l'un des entrée est négatif, la fonction calcule le plus petit multiple commun par rapport à sa valeur absolue
*/

/*
    HLR: L'outil TCAS doit être capable de détecter les conflits entre avions dans son champ de détection
    LLR1 : L'outil doit pouvoir calculer la distance entre deux avion à partir de leurs longitudes et latitudes
    LLR2 : La longitude doit etre comprise entre -180 et 180 sinon le programme génère une excepssion
    LLR3 : La latitude doit etre comprise entre -90 et 90 sinon le programme génère une excepssion
    LLR4: L'outil doit pouvoir comparer la différence d'altitude entre deux avions qui est positif
    LLR5: L'altitude est toujours positif sinon le programme déclenche une excepssion
    LLR6: Le programme détecte s'il y a un conflit entre deux avion en comparant
            la distance et la difference d'altitude à des seuils définit
    LLR7: La fonction detectConflicts vérifie s'il y a un conflit avec un avion dans le champ de détection du TCAS
 */
public class TCAS {


    // La distance verticale en pied pour considérer un danger
    private static int CONFLICT_ALTITUDE_THRESHOLD = 750 ;
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
        if (distance < CONFLICT_DISTANCE_THRESHOLD && relativeAltitude < CONFLICT_ALTITUDE_THRESHOLD) {
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

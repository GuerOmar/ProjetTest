package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TCASTest {

    @Test
    public void testConflictDetection() {
        // Créer deux avions
        Aircraft aircraft1 = Mockito.mock(Aircraft.class);
        Aircraft aircraft2 = Mockito.mock(Aircraft.class);
        // Créer 2 objects TCAS
        TCAS t1 = new TCAS(aircraft1);
        TCAS t2 = new TCAS(aircraft2);

        //Modifier les positions et des mouvements pour entraîner un conflit
        // Configurer le comportement mock pour l'aircraft1
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.65);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(29500);

        // Configurer le comportement mock pour l'aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30000);


        // Vérifier que la méthode isConflict() renvoie true pour ces avions
        assertTrue(t1.isConflict(aircraft2));
        assertTrue(t2.isConflict(aircraft1));

        //Modifier les positions et des mouvements pour ne pas avoir un conflit en ayant des altitudes différentes
        // Configurer le comportement mock pour l'aircraft1
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.65);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(25500);

        // Configurer le comportement mock pour l'aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30000);

        // Vérifier que la méthode isConflict() renvoie false pour ces avions
        assertFalse(t1.isConflict(aircraft2));
        assertFalse(t2.isConflict(aircraft1));

        //Modifier les positions et des mouvements pour ne pas avoir un conflit en ayant une distance tres grande
        // Configurer le comportement mock pour l'aircraft1
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.0);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.0);
        Mockito.when(aircraft1.getAltitude()).thenReturn(30000);

        // Configurer le comportement mock pour l'aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30000);

        // Vérifier que la méthode isConflict() renvoie false pour ces avions
        assertFalse(t1.isConflict(aircraft2));
        assertFalse(t2.isConflict(aircraft1));
    }

    @Test
    void testCalcDistance(){
        // créer un object TCAS
        TCAS t = new TCAS(Mockito.mock(Aircraft.class));

        // On test avec un arrondi pour pouvoir vérifier
        assertEquals(51,Math.round(t.calcDistance(47.0,122.0,47.3,123.0)));
        // on test le cas où a tout les valeurs à 0
        assertEquals(0,t.calcDistance(0.0,0,0,0));

        // La distance retourne toujour une distance positive
        assertTrue(t.calcDistance(-23,-122.0,-47.3,-123.0) >=0 );
        assertTrue(t.calcDistance(-23,-122.0,47.3,123.0) >=0 );

        // Les valeurs des latitudes sont comprises entre -90 et 90
        assertThrows(IllegalStateException.class,() -> t.calcDistance(95,0,0,0) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(-95,0,0,0) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,0,95,0) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,0,-95,0) );
        // Les valeurs des longitudes sont comprises entre -180 et 180
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,185,0,0) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,-185,0,0) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,0,0,185) );
        assertThrows(IllegalStateException.class,() -> t.calcDistance(0,0,0,-185) );


    }

    @Test
    void testCalcRelativeAltitude(){
        // créer un object TCAS
        TCAS t = new TCAS(Mockito.mock(Aircraft.class));

        // Tester l'algorithme
        assertEquals(3000,t.calcRelativeAltitude(30000,27000));
        assertEquals(3000,t.calcRelativeAltitude(27000,30000));

        // L'altitude relative doit etre toujour positive
        assertTrue(t.calcRelativeAltitude(30000,0) >= 0);
        assertTrue(t.calcRelativeAltitude(0,30000) >= 0);

        // Les valeurs des altitudes sont toujours positives
        assertThrows(IllegalStateException.class,() -> t.calcRelativeAltitude(-1000,0));
        assertThrows(IllegalStateException.class,() -> t.calcRelativeAltitude(0,-1000));

    }

    @Test
    void testDetectConflicts() {
        // Créer l'avion ou le TCAS est déployé
        Aircraft aircraft = Mockito.mock(Aircraft.class);
        // Configurer le comportement mock pour l'aircraft1
        Mockito.when(aircraft.getLatitude()).thenReturn(47.65);
        Mockito.when(aircraft.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft.getAltitude()).thenReturn(29500);
        // créer un object TCAS
        TCAS t = new TCAS(aircraft);

        // Créer les avions dans le champ de détection du TCAS
        Aircraft aircraft1 = Mockito.mock(Aircraft.class);
        Aircraft aircraft2 = Mockito.mock(Aircraft.class);

        // Ajouter les avions au champ de détection du TCAS
        t.addAircraft(aircraft1);
        t.addAircraft(aircraft2);

        //Modifier les positions et des mouvements pour entraîner un conflit

        // Configurer le comportement mock pour l'aircraft1 afin d'avoir une distance de danger
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(30000);

        // Configurer le comportement mock pour l'aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(25000);

        assertTrue(t.detectConflicts());

        // Configurer le comportement mock pour l'aircraft1 afin ne plus avoir une distance de danger
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.0);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(30000);

        assertFalse(t.detectConflicts());
    }

}
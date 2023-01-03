package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TCASTest {

    @Test
    public void testConflictDetection() {
        // Créer deux avions avec des positions et des mouvements qui entraînent un conflit
        Aircraft aircraft1 = Mockito.mock(Aircraft.class);
        Aircraft aircraft2 = Mockito.mock(Aircraft.class);
        // create 2 TCAS objects
        TCAS t1 = new TCAS(aircraft1);
        TCAS t2 = new TCAS(aircraft2);

        // Set up mock behavior for aircraft1
        Mockito.when(aircraft1.getLatitude()).thenReturn(47.65);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(29.5);

        // Set up mock behavior for aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30.0);


        // Vérifier que la méthode isConflict() renvoie true pour ces avions
        assertTrue(t1.isConflict(aircraft2));
        assertTrue(t2.isConflict(aircraft1));

        Mockito.when(aircraft1.getLatitude()).thenReturn(47.65);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft1.getAltitude()).thenReturn(25.5);

        // Set up mock behavior for aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30.0);

        // Vérifier que la méthode isConflict() renvoie true pour ces avions
        assertFalse(t1.isConflict(aircraft2));
        assertFalse(t2.isConflict(aircraft1));

        Mockito.when(aircraft1.getLatitude()).thenReturn(47.0);
        Mockito.when(aircraft1.getLongitude()).thenReturn(122.0);
        Mockito.when(aircraft1.getAltitude()).thenReturn(30.0);

        // Set up mock behavior for aircraft2
        Mockito.when(aircraft2.getLatitude()).thenReturn(47.7);
        Mockito.when(aircraft2.getLongitude()).thenReturn(122.4);
        Mockito.when(aircraft2.getAltitude()).thenReturn(30.0);

        // Vérifier que la méthode isConflict() renvoie true pour ces avions
        assertFalse(t1.isConflict(aircraft2));
        assertFalse(t2.isConflict(aircraft1));
    }

    @Test
    void addAircraft() {
    }

    @Test
    void detectConflicts() {
    }
}
package at.technikum.stationdatacollector.controller;

import at.technikum.stationdatacollector.dto.Station;
import at.technikum.stationdatacollector.service.DatabaseService;
import at.technikum.stationdatacollector.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Verwenden von MockitoExtension zur Initialisierung von Mocks
public class CollectorControllerTest {

    @Mock
    private DatabaseService databaseService; // Mock für DatabaseService

    @Mock
    private MessageService messageService; // Mock für MessageService

    @InjectMocks
    private CollectorController collectorController; // Die zu testende Klasse mit injizierten Mocks

    private String customerId;
    private String dbUrl;
    private ArrayList<Station> stations;

    @BeforeEach
    public void setUp() {
        // Initialisieren der Testdaten
        customerId = "1";
        dbUrl = "localhost:30011";
        stations = new ArrayList<>();
        stations.add(new Station(1, 10.0f, 1)); // Hinzufügen einer Beispiel-Station
    }

    @Test
    public void testCollectData() throws Exception {
        // Vorbereiten des Mock-Verhaltens für getStations
        when(databaseService.getStations(customerId, dbUrl)).thenReturn(stations);

        // Aufrufen der zu testenden Methode
        collectorController.collect(customerId, dbUrl);

        // Verifizieren, dass getStations genau einmal aufgerufen wurde
        verify(databaseService, times(1)).getStations(customerId, dbUrl);
        // Verifizieren, dass send genau einmal mit den richtigen Argumenten aufgerufen wurde
        verify(messageService, times(1)).send(1, "10.0", customerId);
    }

    @Test
    public void testCollectEnd() throws Exception {
        // Aufrufen der zu testenden Methode mit "collection ended"
        collectorController.collect(customerId, "collection ended");

        // Verifizieren, dass sendEnd genau einmal aufgerufen wurde
        verify(messageService, times(1)).sendEnd();
    }
}

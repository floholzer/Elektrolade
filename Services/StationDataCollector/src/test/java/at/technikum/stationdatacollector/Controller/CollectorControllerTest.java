package at.technikum.stationdatacollector.Controller;

import at.technikum.stationdatacollector.dto.Station;
import at.technikum.stationdatacollector.Service.DatabaseService;
import at.technikum.stationdatacollector.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class CollectorControllerTest {

    @Mock
    private DatabaseService databaseService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private CollectorController collectorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws Exception {
        // Da die run-Methode nur listen aufruft, testen wir hier nur, ob diese Methode aufgerufen wird.
        doNothing().when(messageService).receive(any(), any(CollectorController.class));

        collectorController.run();

        verify(messageService, times(1)).receive(any(), any(CollectorController.class));
    }

    @Test
    void testCollect() throws Exception {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1, 100.0f, 1));

        when(databaseService.getStations(anyString(), anyString())).thenReturn(new ArrayList<>(stations));

        collectorController.collect("1", "station1");

        verify(messageService, times(1)).send("collection_receiver", "1 100.0", "1");
    }

    @Test
    void testFinalizeCollection() throws Exception {
        collectorController.collect("1", "end");

        verify(messageService, times(1)).send("collection_receiver", "finished", "1");
    }
}

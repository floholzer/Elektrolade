package at.technikum.datacollectionreceiver.Controller;

import at.technikum.datacollectionreceiver.Model.Model;
import at.technikum.datacollectionreceiver.Model.StationData;
import at.technikum.datacollectionreceiver.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class ReceiverControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ReceiverController receiverController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws Exception {
        doNothing().when(messageService).listen(any(), any(ReceiverController.class));

        receiverController.run();

        verify(messageService, times(1)).listen(any(), any(ReceiverController.class));
    }

    @Test
    void testCollectStart() throws Exception {
        String[] message = {"1", "start"};

        receiverController.collect(message);

        StationData stationData = receiverController.getStationData();
        assert stationData != null;
        assert "1".equals(stationData.getCustomer_id());
        assert stationData.getStations().isEmpty();
    }

    @Test
    void testCollectFinished() throws Exception {
        String[] startMessage = {"1", "start"};
        receiverController.collect(startMessage);

        String[] stationMessage = {"1", "station1", "100"};
        receiverController.collect(stationMessage);

        String[] finishedMessage = {"1", "finished"};
        receiverController.collect(finishedMessage);

        verify(messageService, times(1)).sendMessage("pdf_service", "start 1");
        verify(messageService, times(1)).sendMessage("pdf_service", "station1 100");
        verify(messageService, times(1)).sendMessage("pdf_service", "end 1");
    }

    @Test
    void testCollectAddStation() throws Exception {
        String[] startMessage = {"1", "start"};
        receiverController.collect(startMessage);

        String[] stationMessage = {"1", "station1", "100"};
        receiverController.collect(stationMessage);

        StationData stationData = receiverController.getStationData();
        assert stationData != null;
        assert stationData.getStations().size() == 1;
        Model station = stationData.getStations().get(0);
        assert "station1".equals(station.getId());
        assert "100".equals(station.getKwh());
    }
}

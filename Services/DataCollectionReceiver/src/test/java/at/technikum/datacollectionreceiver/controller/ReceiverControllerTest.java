package at.technikum.datacollectionreceiver.controller;

import at.technikum.datacollectionreceiver.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReceiverControllerTest {

    private MessageService messageService;

    // Methode, die vor jedem Test ausgeführt wird, um den MessageService zu mocken und im ReceiverController zu setzen
    @BeforeEach
    public void setUp() {
        // Mocken des MessageService
        messageService = Mockito.mock(MessageService.class);
        // Setzen des gemockten MessageService im ReceiverController
        ReceiverController.messageService = messageService;
    }

    // Test der collect-Methode, wenn normale Stationsdaten empfangen werden
    @Test
    public void testCollectData() throws Exception {
        // Beispielhafte Nachrichten mit Stationsdaten
        String[] message1 = {"1", "1", "10.0"};
        String[] message2 = {"1", "1", "10.0"};

        // Aufrufen der collect-Methode mit den Nachrichten
        ReceiverController.collect(message1);
        ReceiverController.collect(message2);

        // Überprüfen, ob die Daten korrekt in der stationDataMap gespeichert wurden
        assertEquals(2, ReceiverController.stationDataMap.get("1").size());
        assertEquals("10.0", ReceiverController.stationDataMap.get("1").get(0));
        assertEquals("10.0", ReceiverController.stationDataMap.get("1").get(1));
    }

    // Test der collect-Methode, wenn die "data end"-Nachricht empfangen wird
    @Test
    public void testCollectEnd() throws Exception {
        // Beispielhafte Nachrichten mit Stationsdaten und einer "data end"-Nachricht
        String[] message1 = {"1", "1", "10.0"};
        String[] message2 = {"1", "1", "10.0"};
        String[] messageEnd = {"data end"};

        // Aufrufen der collect-Methode mit den Nachrichten
        ReceiverController.collect(message1);
        ReceiverController.collect(message2);
        ReceiverController.collect(messageEnd);

        // Überprüfen, ob die aggregierte Nachricht gesendet wurde
        verify(messageService, times(1)).send("1;2;10.0,10.0,10.0,10.0");
        // Überprüfen, ob die stationDataMap geleert wurde
        assertEquals(0, ReceiverController.stationDataMap.size());
    }
}

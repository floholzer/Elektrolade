package at.technikum.stationapi.controller;

import at.technikum.stationapi.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RESTControllerTest {

    @InjectMocks
    private RESTController restController;

    @Mock
    private Resource resource;

    @Mock
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void collectInvoice_ThrowsException_WhenMessageServiceThrowsException() throws Exception {
        int customerId = -12;

        doThrow(new IOException()).when(messageService).send(String.valueOf(customerId));

        assertThrows(ResponseStatusException.class, () -> restController.collectInvoice(customerId));
    }

    @Test
    public void collectInvoice_Succeeds_WhenCustomerIdIsValid() throws Exception {
        int customerId = 1;

        doReturn(true).when(messageService).send(String.valueOf(customerId));

        restController.collectInvoice(customerId);
    }

    @Test // GET Request
    public void downloadFile_NoFileFound() {

        String customerId = "123";

        when(resource.exists()).thenReturn(false);

        ResponseEntity<Resource> response = restController.downloadFile(customerId);

        assertEquals(404, response.getStatusCodeValue());
    }
}
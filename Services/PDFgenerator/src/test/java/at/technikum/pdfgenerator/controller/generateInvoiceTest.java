package at.technikum.pdfgenerator.controller;

import at.technikum.pdfgenerator.controller.PDFController;
import at.technikum.pdfgenerator.dto.Customer;
import at.technikum.pdfgenerator.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SpringBootTest
public class generateInvoiceTest {

    @Mock
    private DataService dataService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }
/*
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        File file = new File("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-1.pdf");
        if (file.exists()) {
            file.delete();
        }
    }
*/
    @Test
    void testGenerateInvoice() throws Exception {

        // Test data
        String messageData = "1;1;10,20,30|1;2;5,15";

        // Call the method
        PDFController.generateInvoice(messageData);

        // Verify the PDF was created
        File file = new File("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-1.pdf");
        assertTrue(file.exists(), "PDF file should be created");
    }
}

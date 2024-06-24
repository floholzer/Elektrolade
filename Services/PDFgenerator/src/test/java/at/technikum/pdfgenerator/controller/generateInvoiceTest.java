package at.technikum.pdfgenerator.controller;

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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class generateInvoiceTest{

    @Mock
    private DataService dataService;

    @BeforeEach
    void setUp() {
        // Set the mocked dataService in PDFController
        PDFController.setDataService(dataService);
    }

    @Test
    void testGenerateInvoice() throws Exception {
        // Setup mock customer
        /*Customer mockCustomer = new Customer();
        mockCustomer.setFirst_name("John");
        mockCustomer.setLast_name("Doe");
        when(dataService.getCustomer(1)).thenReturn(mockCustomer);
        */
        // Test data
        String messageData = "1;1;10,20,30|1;2;5,15";

        // Call the method
        PDFController.generateInvoice(messageData); //File Path Error?

        // Verify the PDF was created
        File file = new File("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-1.pdf");
        assertTrue(file.exists(), "PDF file should be created");
    }
}

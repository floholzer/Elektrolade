package at.technikum.pdfgenerator.controller;

import at.technikum.pdfgenerator.dto.Customer;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import at.technikum.pdfgenerator.service.DataService;
import at.technikum.pdfgenerator.service.MessageService;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.time.LocalDateTime;

public class PDFController {
    public static final MessageService messageService = new MessageService();
    public static final DataService dataService = new DataService();

    public void generateInvoice() throws Exception {
        int CustomerID = 1; // TODO Replace with actual customer ID from Message Queue
        Customer customer = dataService.getCustomer(CustomerID);

        try {
            PdfWriter writer = new PdfWriter("src/main/resources/files/invoice-"+customer.getId()+".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            Image img = new Image(ImageDataFactory.create("src/main/resources/images/logo.png"));

            doc.add(img);
            doc.add(new Paragraph("Invoice").setFontSize(18).setFontColor(ColorConstants.BLACK));
            doc.add(new Paragraph(LocalDateTime.now().toString()).setFontSize(12).setFontColor(ColorConstants.BLACK));
            doc.add(new Paragraph("Customer: " + customer.getFirst_name() + " " + customer.getLast_name()).setFontSize(12).setFontColor(ColorConstants.BLACK));

            // Cost Table
            float[] columnWidths = {1, 5, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("Nr.").setFontSize(12)));
            table.addHeaderCell(new Cell().add(new Paragraph("Station").setFontSize(12)));
            table.addHeaderCell(new Cell().add(new Paragraph("kwH").setFontSize(12)));
            table.addHeaderCell(new Cell().add(new Paragraph("Cost").setFontSize(12)));

            // Example invoice item TODO Replace with actual invoice items from Message Queue
            table.addCell("1");
            table.addCell("Station 1");
            table.addCell("25");
            table.addCell("10,00 €");

            doc.add(table);

            // Total sum TODO Replace with actual total sum from Message Queue
            doc.add(new Paragraph("Total: 10,00 €").setFontSize(12).setFontColor(ColorConstants.BLACK));

            doc.close();
            System.out.println(">> PDFGenerator: 'Invoice generated successfully'");
        } catch (Exception e) {
            throw new IOException("Error while generating PDF" + e.getMessage());
        }

    }
}

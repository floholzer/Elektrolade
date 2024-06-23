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
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFController {
    public static final DataService dataService = new DataService();

    public static void generateInvoice(String messageData) throws Exception {
        String[] stationDataArray = messageData.split("\\|");
        int customerId = Integer.parseInt(stationDataArray[0].split(";")[0]);
        Customer customer = dataService.getCustomer(customerId);


        try {
            PdfWriter writer = new PdfWriter("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-"+customerId+".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            Image img = new Image(ImageDataFactory.create("Services/PDFgenerator/src/main/resources/images/logo.jpeg"));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String formattedDateTime = now.format(formatter);


            doc.add(img);
            doc.add(new Paragraph("Invoice").setFontSize(18).setFontColor(ColorConstants.BLACK));
            doc.add(new Paragraph(formattedDateTime).setFontSize(12).setFontColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.RIGHT));
            //doc.add(new Paragraph(LocalDateTime.now().toString()).setFontSize(12).setFontColor(ColorConstants.BLACK));
            doc.add(new Paragraph("Customer: " + customer.getFirst_name() + " " + customer.getLast_name()).setFontSize(12).setFontColor(ColorConstants.BLACK));

            for (String stationData : stationDataArray) {
                String[] data = stationData.split(";");
                String stationId = data[1];
                String[] kwhValues = data[2].split(",");

                doc.add(new Paragraph("Station: " + stationId).setFontSize(12).setFontColor(ColorConstants.BLACK));

                // Cost Table
                float[] columnWidths = {1, 5, 2};
                Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

                table.addHeaderCell(new Cell().add(new Paragraph("Nr.").setFontSize(12)));
                table.addHeaderCell(new Cell().add(new Paragraph("kwH").setFontSize(12)));
                table.addHeaderCell(new Cell().add(new Paragraph("Cost").setFontSize(12)));

                double totalCost = 0;
                for (int i = 0; i < kwhValues.length; i++) {
                    double kwh = Double.parseDouble(kwhValues[i]);
                    double cost = kwh * 0.65; // Assuming a cost of 0.65 per kWh
                    totalCost += cost;

                    table.addCell(String.valueOf(i + 1));
                    table.addCell(String.valueOf(kwh));
                    table.addCell(String.format("%.2f €", cost));
                }

                doc.add(table);

                // Total sum
                doc.add(new Paragraph("Total: " + String.format("%.2f €", totalCost)).setFontSize(12).setFontColor(ColorConstants.BLACK));
            }

            doc.close();
            System.out.println("PDFGenerator: 'Invoice generated successfully'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error while generating PDF" + e.getMessage(), e);
        }

    }
}

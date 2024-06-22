package at.technikum.stationui;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    private static final String API_URL = "http://127.0.0.1:8080/invoices/";

    @FXML
    private TextField customerIDField;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onGenerateInvoiceButtonClick() {
        String customerID = customerIDField.getText();  // assuming customerIDField is a TextField instance

        if (customerID == null || customerID.wait().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Customer ID missing");
            return;
        }

        String fileName = "Invoice_" + customerID + ".pdf";
        String apiUrl = API_URL + customerID + "/download";

        try {
            // Generiere die Rechnung
            var generateRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/invoices"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(customerID)).build();
            var generateResponse = HttpClient.newHttpClient()
                    .send(generateRequest, HttpResponse.BodyHandlers.ofString());

            // Überprüfe, ob die Rechnung erfolgreich generiert wurde
            if (generateResponse.statusCode() == 200) {
                // Hier wird der Dateiname basierend auf der Kunden-ID erstellt
                String fileName = "Invoice_" + customerID + ".pdf";  // Beispiel-Dateiname

                // Erstellen Sie eine HTTP-Anfrage, um die PDF-Datei herunterzuladen
                HttpRequest downloadRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/v1/invoices/" + customerID + "/download"))
                        .build();

                // Senden Sie die Anfrage und erhalten Sie die Antwort
                HttpResponse<java.io.InputStream> downloadResponse = HttpClient.newHttpClient()
                        .send(downloadRequest, HttpResponse.BodyHandlers.ofInputStream());

                // Überprüfen Sie, ob die Anfrage erfolgreich war (Statuscode 200)
                if (downloadResponse.statusCode() == 200) {
                    // Pfad, in dem die heruntergeladene PDF-Datei gespeichert wird
                    // Holen Sie sich den Benutzerverzeichnis-Pfad
                    String userHome = System.getProperty("user.home");

                    // Pfad zum "Downloads"-Ordner hinzufügen
                    String downloadFolderPath = userHome + File.separator + "Downloads";

                    // Erstellen Sie den vollständigen Pfad zum Speichern der heruntergeladenen PDF-Datei
                    String downloadFilePath = downloadFolderPath + File.separator + fileName;

                    // Schreiben Sie die heruntergeladene PDF-Datei in das Dateisystem
                    try (BufferedInputStream inputStream = new BufferedInputStream(downloadResponse.body());
                         FileOutputStream fileOutputStream = new FileOutputStream(downloadFilePath)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice downloaded successfully");
                }
            } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error occurred while downloading the invoice");
                }
            } catch(IOException | InterruptedException e){
                showAlert(Alert.AlertType.WARNING, "Error", "REST-API Error \n" + e.toString());
            }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }

}
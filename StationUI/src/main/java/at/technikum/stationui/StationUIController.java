package at.technikum.stationui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StationUIController {

    private static final String API_URL = "http://127.0.0.1:8080/invoices/";

    @FXML
    private TextField customerIDField;

    @FXML
    protected void onGenerateInvoiceButtonClick() throws Exception {
        String customerID = customerIDField.getText();

        // Überprüfen, ob der Wert eine Zahl ist
        try {
            Integer.parseInt(customerID);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Error", "Customer ID must be a number");
            return;
        }



        // POST-Request senden
        var generateRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + customerID))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(customerID))
                .build();

        try {
            var generateResponse = HttpClient.newHttpClient().send(generateRequest, HttpResponse.BodyHandlers.ofString());

            // Überprüfen, ob die Rechnung erfolgreich generiert wurde
            if (generateResponse.statusCode() == 200) {

                // Überprüfen Sie alle 2 Sekunden das Verzeichnis auf neue Dateien
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    File folder = new File("StationAPI/src/main/java/at/technikum/stationapi/files");
                    File[] listOfFiles = folder.listFiles();

                    if (listOfFiles != null && listOfFiles.length > 1) {
                        // GET-Request senden
                        try {
                            URL url = new URL(API_URL + customerID);
                            String userHome = System.getProperty("user.home");
                            String outFileName = userHome+"/downloads/invoice-" + customerID + ".pdf";
                            Path outPath = Paths.get(outFileName);

                            try (
                                    InputStream in = url.openStream();
                                 OutputStream out = Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
                            ) {

                                byte[] buffer = new byte[1024];
                                int bytesRead;

                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }

                                showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice downloaded successfully to " + outFileName);

                                // delete the file after downloading
                                Files.delete(Paths.get("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-" + customerID + ".pdf"));
                            } catch (Exception e) {
                                showAlert(Alert.AlertType.WARNING, "Error", "Error occurred while downloading the invoice: " + e.getMessage());
                            }
                        } catch (Exception e) {
                            showAlert(Alert.AlertType.WARNING, "Error", "Invalid URL: " + e.getMessage());
                        }

                        // Stoppen Sie den Executor-Service, nachdem die Datei gefunden wurde
                        executorService.shutdown();
                    }
                }, 0, 2, TimeUnit.SECONDS);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error occurred while generating the invoice");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "REST-API Error \n" + e.getMessage());
        }
        customerIDField.clear();
    }
    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    protected void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }

}
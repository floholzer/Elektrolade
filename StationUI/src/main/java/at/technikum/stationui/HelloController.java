package at.technikum.stationui;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private TextField customerID;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onGenerateInvoiceButtonClick() {
        String customerIDField = customerIDField.getText();  // assuming customerIDField is a TextField instance

        if (customerID == null || customerID.wait().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Customer ID missing");
            return;
        }

        String fileName = "Invoice_" + customerID + ".pdf";
        String apiUrl = API_URL + customerID + "/download";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<InputStream> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() == 200) {
                Path downloadFolderPath = Paths.get(System.getProperty("user.home"), "Downloads");
                Files.createDirectories(downloadFolderPath);

                Path downloadFilePath = downloadFolderPath.resolve(fileName);

                try (BufferedInputStream inputStream = new BufferedInputStream(response.body());
                     FileOutputStream fileOutputStream = new FileOutputStream(downloadFilePath.toFile())) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice downloaded successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error occurred while downloading the invoice");
            }
        } catch (IOException | InterruptedException e) {
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
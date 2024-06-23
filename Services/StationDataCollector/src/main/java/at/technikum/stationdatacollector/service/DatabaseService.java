package at.technikum.stationdatacollector.service;

import at.technikum.stationdatacollector.dto.Station;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

// Verwaltet den Datenbankzugriff
@Service
public class DatabaseService {

    // Methode zum Herstellen einer Verbindung zur Datenbank
    private static Connection connect(String dbUrl) throws SQLException {
        String connectionString="jdbc:postgresql://"+dbUrl+"/stationdb";
        return DriverManager.getConnection(connectionString, "postgres", "postgres");
    }

    // Ruft die Informationen der Stationen für einen bestimmten Kunden ab
    public ArrayList<Station> getStations(String customerId, String dbUrl) throws SQLException {
        ArrayList<Station> stations = new ArrayList<>();

        try (Connection conn = connect(dbUrl)) {
            String query = "SELECT id, kwh, customer_id FROM charge WHERE customer_id = " + customerId + ";";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Station station = new Station(
                        resultSet.getInt(1),
                        resultSet.getFloat(2),
                        resultSet.getInt(3)
                );
                stations.add(station);
            }
        }
        return stations;
    }
}

package at.technikum.stationapi.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataService {
    private static Connection connect() throws SQLException {
        String connectionString="jdbc:postgresql://localhost:30001/customerdb";
        return DriverManager.getConnection(connectionString, "postgres", "postgres");
    }
}

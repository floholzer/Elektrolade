package at.technikum.datacollectiondispatcher.service;

import at.technikum.datacollectiondispatcher.dto.Station;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class DataService {

    private static Connection connect() throws SQLException {
        String connectionString="jdbc:postgresql://localhost:30002/stationdb";
        return DriverManager.getConnection(connectionString, "postgres", "postgres");
    }

    public static ArrayList<Station> getStations() throws SQLException {
        ArrayList<Station> stations = new ArrayList<>();

        try ( Connection conn = connect() ) {
            String query = "SELECT id, db_url, lat, lng FROM station;";
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet resultSet = stmt.executeQuery(query);
            while( resultSet.next()) {
                Station station = new Station(
                        resultSet.getInt("id"),
                        resultSet.getString("db_url"),
                        resultSet.getFloat("lng"),
                        resultSet.getFloat("lat")
                );
                stations.add(station);
            }
        }
        return stations;
    }
}

package at.technikum.stationdatacollector.Service;

import java.sql.*;
import java.util.ArrayList;

//Verwaltet den Datenbankzugriff.
public class DatabaseService {

    private static Connection connect(String to) throws SQLException {
        String connectionString="jdbc:postgresql://"+ to + "/stationdb";
        return DriverManager.getConnection(connectionString, "postgres", "postgres");
    }


//    getStations stellt eine Verbindung zur Datenbank her und ruft die Informationen der Stationen für einen bestimmten Kunden ab.
public static ArrayList<Station> getStations(String customer_id, String to) throws SQLException {
    ArrayList<Station> stations = new ArrayList<>();

    try ( Connection conn = connect(to) ) {
        String query = "SELECT id, kwh, customer_id FROM charge WHERE customer_id = " + customer_id +";";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery();
        while( resultSet.next()) {
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

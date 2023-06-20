package main.orm;

import main.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItineraryDAO {
    public void insert(Itinerary i) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Itinerary ( id, name) VALUES ( ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, i.getId());
        ps.setString(2, i.getName());

        ps.executeUpdate();
        ps.close();
    }

    public void delete(Itinerary i) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql1 = "DELETE FROM Itinerary WHERE id = ?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, i.getId());
        ps1.executeUpdate();
        ps1.close();

        //rimuove associazione con le opere d'arte
        String sql2 = "DELETE FROM Artwork_Itinerary WHERE Itinerary = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, i.getId());
        ps2.close();
    }
}

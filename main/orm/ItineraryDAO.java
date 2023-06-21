package main.orm;

import main.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

        //removes association with artworks
        String sql2 = "DELETE FROM Artwork_Itinerary WHERE itinerary = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, i.getId());
        ps2.executeUpdate();
        ps2.close();
    }

    public Itinerary getTransitive(int id) throws SQLException {
        //function also instantiates Artwork objects

        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT id, Itinerary.name i_name, code, Artwork.name a_name, artist author, status, payload FROM Itinerary, Artwork_Itinerary, Artwork WHERE id = ? , Artwork_Itinerary.itinerary = id, code = Artwork_Itinerary.artwork";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        ArrayList<Artwork> artworks = new ArrayList<>();
        while (rs.next()) {
            ArtworkStatus as = null;
            switch (rs.getInt("status")) {
                //status integers defined in DB
                case 1:
                    as = new OnDisplay();
                    break;
                case 2:
                    as = new UnderMaintainance(rs.getString("payload"));
                    break;
                case 3:
                    as = new OnLoan(rs.getString("payload"));
                    break;
            }
            Artwork a = new Artwork(rs.getInt("code"), rs.getString("a_name"), rs.getString("author"), as);
            artworks.add(a);
        }

        return new Itinerary(rs.getInt("id"), rs.getString("i_name"), artworks);
    }
}

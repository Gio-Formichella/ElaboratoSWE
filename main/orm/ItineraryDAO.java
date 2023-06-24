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

    //deletes itinerary and associations with artworks
    public void delete(Itinerary i) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        //removes association with artworks
        String sql2 = "DELETE FROM Artwork_Itinerary WHERE itinerary = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, i.getId());
        ps2.executeUpdate();
        ps2.close();

        String sql1 = "DELETE FROM Itinerary WHERE id = ?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, i.getId());
        ps1.executeUpdate();
        ps1.close();
    }

    public Itinerary getTransitive(int id) throws SQLException {
        //function also instantiates Artwork objects

        Connection con = ConnectionManager.getConnection();

        String sql = """
                SELECT id, itinerary.name as i_name, code, artwork.name as a_name, artist as author, status, payload
                FROM (Itinerary left join Artwork_Itinerary on id=itinerary) left join Artwork on artwork = code
                WHERE id = ?""";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Itinerary i = null;
        ArrayList<Artwork> artworks = new ArrayList<>();

        //Query result can be: no tuple, single tuple with empty attributes for artwork and full for itinerary,
        // one or more tuples with both itinerary and artwork attributes full
        if (rs.next()) {  //requested itinerary exists
            int i_id = rs.getInt("id");
            String i_name = rs.getString("i_name");
            if (rs.getInt("status") != 0) { //itinerary has at least one artwork
                rs.previous(); //moves cursor to the start
                while (rs.next()) {
                    int code = rs.getInt("code");
                    String a_name = rs.getString("a_name");
                    String author = rs.getString("author");

                    ArtworkStatus as = null;
                    switch (rs.getInt("status")) {
                        case 1:
                            as = new OnDisplay();
                            break;
                        case 2:
                            as = new UnderMaintenance(rs.getString("payload"));
                            break;
                        case 3:
                            as = new OnLoan(rs.getString("payload"));
                            break;
                    }
                    artworks.add(new Artwork(code, a_name, author, as));
                }
            }
            i = new Itinerary(i_id, i_name, artworks);
        }
        return i;
    }

    //creates association between and itinerary and an artwork
    public void addArtworkToItinerary(Itinerary i, Artwork a) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Artwork_Itinerary ( artwork, itinerary) VALUES ( ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, a.getCode());
        ps.setInt(2, i.getId());

        ps.executeUpdate();
        ps.close();
    }
}

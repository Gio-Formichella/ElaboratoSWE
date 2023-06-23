package main.orm;

import main.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArtworkDAO {

    public Artwork get(int code) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT code, name, artist, status, payload FROM Artwork WHERE code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ResultSet rs = ps.executeQuery();

        Artwork a = null;
        if (rs.next()) {
            String name = rs.getString("name");
            String artist = rs.getString("artist");
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
            a = new Artwork(code, name, artist, as);
        }
        return a;  //returns null if no artwork is found
    }

    public void insert(Artwork a) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Artwork ( code, name, artist, status, payload) VALUES ( ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, a.getCode());
        ps.setString(2, a.getName());
        ps.setString(3, a.getAuthor());

        ArtworkStatus as = a.getArtworkStatusObject();
        if (as.getClass() == OnDisplay.class) {
            ps.setInt(4, 1);
            ps.setString(5, null);
        } else if (as.getClass() == UnderMaintenance.class) {
            ps.setInt(4, 2);
            UnderMaintenance um = (UnderMaintenance) as;
            ps.setString(5, um.getEstimatedCompletion());
        } else if (as.getClass() == OnLoan.class) {
            ps.setInt(4, 3);
            OnLoan ol = (OnLoan) as;
            ps.setString(5, ol.getBorrowingMuseum());
        }

        ps.executeUpdate();
        ps.close();
    }

    public void delete(Artwork a) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "DELETE FROM Artwork WHERE code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, a.getCode());
        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<Artwork> getAll() throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM Artwork";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Artwork> artworks = new ArrayList<>();
        if (rs.next()) {
            int code = rs.getInt("code");
            String name = rs.getString("name");
            String artist = rs.getString("artist");
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
            artworks.add(new Artwork(code, name, artist, as));
        }
        return artworks;
    }
}

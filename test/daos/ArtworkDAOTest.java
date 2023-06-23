package test.daos;

import main.DomainModel.Artwork;
import main.DomainModel.OnDisplay;
import main.DomainModel.OnLoan;
import main.DomainModel.UnderMaintenance;
import main.orm.ArtworkDAO;
import main.orm.ConnectionManager;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArtworkDAOTest {

    @Test
    void get() {
        ArtworkDAO dao = new ArtworkDAO();
        Artwork a = new Artwork(1, "AnArtwork", "Gio", new OnDisplay());
        try {
            dao.insert(a);
            Artwork retrieved = dao.get(a.getCode());
            assertEquals(retrieved.getCode(), a.getCode());
            assertEquals(retrieved.getName(), a.getName());
            assertEquals(retrieved.getAuthor(), a.getAuthor());
            assertEquals(retrieved.getStatus(), a.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(a);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void insert() {
        ArtworkDAO dao = new ArtworkDAO();
        Artwork a1 = new Artwork(-1, "FirstArtwork", "Gio1", new OnDisplay());
        Artwork a2 = new Artwork(-2, "SecondArtwork", "Gio2", new UnderMaintenance("30/07/2023"));
        Artwork a3 = new Artwork(-3, "ThirdArtwork", "Gio3", new OnLoan("Museo Pecci"));

        try {
            dao.insert(a1);
            dao.insert(a2);
            dao.insert(a3);

            Artwork retrieved1 = dao.get(a1.getCode());
            assertEquals(retrieved1.getCode(), a1.getCode());
            assertEquals(retrieved1.getName(), a1.getName());
            assertEquals(retrieved1.getAuthor(), a1.getAuthor());
            assertEquals(retrieved1.getStatus(), a1.getStatus());

            Artwork retrieved2 = dao.get(a2.getCode());
            assertEquals(retrieved2.getCode(), a2.getCode());
            assertEquals(retrieved2.getName(), a2.getName());
            assertEquals(retrieved2.getAuthor(), a2.getAuthor());
            assertEquals(retrieved2.getStatus(), a2.getStatus());

            Artwork retrieved3 = dao.get(a3.getCode());
            assertEquals(retrieved3.getCode(), a3.getCode());
            assertEquals(retrieved3.getName(), a3.getName());
            assertEquals(retrieved3.getAuthor(), a3.getAuthor());
            assertEquals(retrieved3.getStatus(), a3.getStatus());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(a1);
                dao.delete(a2);
                dao.delete(a3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void delete() {
        ArtworkDAO dao = new ArtworkDAO();
        Artwork a = new Artwork(1, "AnArtwork", "Gio", new OnDisplay());
        try {
            dao.insert(a);
            dao.delete(a);

            assertNull(dao.get(a.getCode()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                Connection con = ConnectionManager.getConnection();

                String sql = "DELETE FROM Artwork WHERE code = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, a.getCode());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getAll() {
        ArtworkDAO dao = new ArtworkDAO();
        try {
            assertEquals(dao.getAll().getClass(), ArrayList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateStatus() {
        ArtworkDAO dao = new ArtworkDAO();
        Artwork a = new Artwork(-1, "TestArtwork", "Gio", new OnDisplay());

        try {
            dao.insert(a);

            UnderMaintenance um = new UnderMaintenance("11/07/2023");
            a.setStatus(um);
            dao.updateStatus(a);
            Artwork retrieved = dao.get(a.getCode());
            assertEquals(retrieved.getStatus(), a.getStatus());

            OnLoan ol = new OnLoan("Collezione di Gio");
            a.setStatus(ol);
            dao.updateStatus(a);
            Artwork retrieved2 = dao.get(a.getCode());
            assertEquals(retrieved2.getStatus(), a.getStatus());

            OnDisplay od = new OnDisplay();
            a.setStatus(od);
            dao.updateStatus(a);
            Artwork retrieved3 = dao.get(a.getCode());
            assertEquals(retrieved3.getStatus(), a.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(a);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
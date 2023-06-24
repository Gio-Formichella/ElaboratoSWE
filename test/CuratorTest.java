package test;

import main.DomainModel.Artwork;
import main.DomainModel.Itinerary;
import main.DomainModel.OnDisplay;
import main.DomainModel.UnderMaintenance;
import main.business_logic.Curator;
import main.orm.ArtworkDAO;
import main.orm.ConnectionManager;
import main.orm.ItineraryDAO;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CuratorTest {

    @Test
    void addItinerary() {
        Curator c = new Curator();
        int itinerary_id = 89;
        String itinerary_name = "Egitto";
        try {
            c.addItinerary(itinerary_id, itinerary_name);

            ItineraryDAO dao = new ItineraryDAO();
            Itinerary i = dao.getTransitive(itinerary_id);

            assertEquals(i.getId(), itinerary_id);
            assertEquals(i.getName(), itinerary_name);
            assertTrue(i.getArtworks().isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ItineraryDAO dao = new ItineraryDAO();
                dao.delete(new Itinerary(itinerary_id, itinerary_name, new ArrayList<>()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void addArtwork() {
        Curator c = new Curator();
        Itinerary i = new Itinerary(-1, "TestItinerary", new ArrayList<>());
        Artwork a = new Artwork(-1, "TestArtwork", "Gio", new OnDisplay());
        Artwork a2 = new Artwork(-2, "TestArtwork", "Gio", new OnDisplay());
        try {
            ItineraryDAO dao = new ItineraryDAO();
            dao.insert(i);

            c.addArtwork(i, a);
            c.addArtwork(i, a2);
            assertEquals(i.getArtworks().size(), 2);

            //retrieving association tuples
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT count(artwork) as count FROM artwork_itinerary WHERE itinerary = ? ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, i.getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(rs.getInt("count"), 2);
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                ItineraryDAO dao = new ItineraryDAO();
                dao.delete(i);
                ArtworkDAO adao = new ArtworkDAO();
                adao.delete(a);
                adao.delete(a2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void viewArtworks() {
        Curator c = new Curator();
        try {
            assertEquals(c.viewArtworks().getClass(), ArrayList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void modifyStatus() {
        Curator c = new Curator();
        Artwork a = new Artwork(-1, "TestArtwork", "Gio", new OnDisplay());
        OnDisplay od = new OnDisplay();
        UnderMaintenance um = new UnderMaintenance("18/07/2023");
        ArtworkDAO dao = new ArtworkDAO();
        try {
            dao.insert(a);
            c.modifyStatus(a, od);
            assertEquals(a.getStatus(), od.getStatus());
            Artwork retrieved1 = dao.get(a.getCode());
            assertEquals(retrieved1.getStatus(), a.getStatus());

            c.modifyStatus(a, um);
            assertEquals(a.getStatus(), um.getStatus());
            Artwork retrieved2 = dao.get(a.getCode());
            assertEquals(retrieved2.getStatus(), a.getStatus());
        } catch (SQLException | MessagingException e) {
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
    void cancelItinerary() {
        Curator c = new Curator();
        Itinerary i = new Itinerary(-1, "TestItinerary", new ArrayList<>());
        try {
            c.addItinerary(i.getId(), i.getName());
            c.cancelItinerary(i);

            ItineraryDAO dao = new ItineraryDAO();
            assertNull(dao.getTransitive(i.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ItineraryDAO dao = new ItineraryDAO();
                dao.delete(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
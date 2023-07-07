package test.daos;

import main.DomainModel.Artwork;
import main.DomainModel.Itinerary;
import main.DomainModel.OnDisplay;
import main.DomainModel.Visit;
import main.orm.ArtworkDAO;
import main.orm.ConnectionManager;
import main.orm.ItineraryDAO;
import main.orm.VisitDAO;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItineraryDAOTest {

    @Test
    void insert() {
        Itinerary i = new Itinerary(89, "Egitto", new ArrayList<>());
        ItineraryDAO dao = new ItineraryDAO();
        try {
            dao.insert(i);

            // retrieving itinerary
            Itinerary retrieved = dao.getTransitive(i.getId());
            assertEquals(retrieved.getId(), i.getId());
            assertEquals(retrieved.getName(), i.getName());
            assertTrue(i.getArtworks().size() == retrieved.getArtworks().size() && i.getArtworks().containsAll(retrieved.getArtworks()) && retrieved.getArtworks().containsAll(i.getArtworks()));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally { //removing inserted tuple
            try {
                dao.delete(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void delete() {
        ItineraryDAO dao = new ItineraryDAO();
        Itinerary i = new Itinerary(2, "RomaAntica", new ArrayList<>());

        try {
            dao.insert(i);
            dao.delete(i);
            assertNull(dao.getTransitive(i.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {  //if something went wrong with delete method, manual deletion of inserted tuple
                Connection con = ConnectionManager.getConnection();
                String sql1 = "DELETE FROM Itinerary WHERE id = ?";
                PreparedStatement ps1 = con.prepareStatement(sql1);
                ps1.setInt(1, i.getId());
                ps1.executeUpdate();
                ps1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void addArtworkToItinerary() {
        Itinerary i = new Itinerary(999, "TestItinerary", new ArrayList<>());
        Artwork a = new Artwork(998, "TestArtwork", "Gio", new OnDisplay());
        ItineraryDAO idao = new ItineraryDAO();
        ArtworkDAO adao = new ArtworkDAO();
        try {
            adao.insert(a);
            idao.insert(i);

            idao.addArtworkToItinerary(i, a);

            //retrieving tuple
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT artwork, itinerary FROM artwork_itinerary WHERE itinerary = ? AND artwork = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, i.getId());
            ps.setInt(2, a.getCode());
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(rs.getInt("itinerary"), i.getId());
            assertEquals(rs.getInt("artwork"), a.getCode());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                idao.delete(i);
                adao.delete(a);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getTransitive() {
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork a1 = new Artwork(-1, "TestArtwork1", "TestAuthor1", new OnDisplay());
        Artwork a2 = new Artwork(-2, "TestArtwork2", "TestAuthor2", new OnDisplay());
        artworks.add(a1);
        artworks.add(a2);
        Itinerary i = new Itinerary(-1, "TestItinerary", artworks);
        try {
            //set up
            ArtworkDAO adao = new ArtworkDAO();
            adao.insert(a1);
            adao.insert(a2);
            ItineraryDAO idao = new ItineraryDAO();
            idao.insert(i);
            idao.addArtworkToItinerary(i, a1);
            idao.addArtworkToItinerary(i, a2);

            //test
            Itinerary retrieved = idao.getTransitive(i.getId());
            assertEquals(i.getId(), retrieved.getId());
            assertEquals(i.getName(), retrieved.getName());
            assertEquals(i.getArtworks().size(), retrieved.getArtworks().size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                //tear down
                ItineraryDAO idao = new ItineraryDAO();
                idao.delete(i);
                ArtworkDAO adao = new ArtworkDAO();
                adao.delete(a1);
                adao.delete(a2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void inUse() {
        Itinerary i1 = new Itinerary(-1, "inUse", new ArrayList<>());
        Itinerary i2 = new Itinerary(-2, "NotInUse", new ArrayList<>());

        VisitDAO vdao = new VisitDAO();
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        itineraries.add(i1);
        Visit v = new Visit(-1, "2020-12-12", "12:00:00", 0, 0, "Italiano", itineraries);

        ItineraryDAO idao = new ItineraryDAO();
        try {
            idao.insert(i2);
            idao.insert(i1);
            vdao.insert(v);

            assertTrue(idao.inUse(i1));
            assertFalse(idao.inUse(i2));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(v.getCode());
                idao.delete(i1);
                idao.delete(i2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
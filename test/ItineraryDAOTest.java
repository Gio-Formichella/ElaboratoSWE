package test;

import main.DomainModel.Artwork;
import main.DomainModel.Itinerary;
import main.DomainModel.OnDisplay;
import main.orm.ConnectionManager;
import main.orm.ItineraryDAO;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItineraryDAOTest {

    @Test
    void testInsert() {
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
    public void testDelete() {
        ItineraryDAO dao = new ItineraryDAO();
        Itinerary i = new Itinerary(2, "RomaAntica", new ArrayList<>());

        try {
            dao.insert(i);
            //TODO metodo addArtwork(Artwork, Itinerary) aggiunge relazione opera itinerario, delete rimuove anche questa
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
    public void testAddArtworkToItinerary() {
        Itinerary i = new Itinerary(999, "TestItinerary", new ArrayList<>());
        Artwork a = new Artwork(999, "TestArtwork", "Gio", new OnDisplay());
        ItineraryDAO dao = new ItineraryDAO();

        //TODO artworkDAO deve inserire artwork in tabella artwork
        try{
            dao.addArtworkToItinerary(i, a);

            //retrieving tuple
            Connection con = ConnectionManager.getConnection();
            String sql = "SELECT artwork, itinerary FROM artwork_itinerary WHERE itinerary = ? AND artwork = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, i.getId());
            ps.setInt(2, a.getCode());
            ResultSet rs = ps.executeQuery();

            assertEquals(rs.getInt("itinerary"), i.getId());
            assertEquals(rs.getInt("artwork"), a.getCode());
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                dao.delete(i);
                //TODO deve essere rimosso artwork
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
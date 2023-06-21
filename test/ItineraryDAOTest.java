package test;

import main.DomainModel.Itinerary;
import main.orm.ItineraryDAO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    }
}
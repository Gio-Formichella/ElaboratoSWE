package test;

import main.DomainModel.Itinerary;
import main.business_logic.Curator;
import main.orm.ItineraryDAO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            try{
                ItineraryDAO dao = new ItineraryDAO();
                dao.delete(new Itinerary(itinerary_id, itinerary_name, new ArrayList<>()));
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    @Test
    void addArtwork() {
    }

    @Test
    void viewArtworks() {
    }

    @Test
    void modifyStatus() {
    }

    @Test
    void cancelItinerary() {
    }
}
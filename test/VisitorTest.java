package test;

import main.DomainModel.*;
import main.business_logic.VisitorController;
import main.orm.BookingDAO;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VisitorTest {
    @Test
    public void cancelBooking(){
        VisitorController v = new VisitorController();

        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(194, false, visit, visitor, 5);
        BookingDAO bdao = new BookingDAO();
        try{
            v.bookVisit(visit, visitor, b.getCode(), 4);
            v.cancelBooking(b);
            assertTrue(bdao.get(b.getCode()).isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

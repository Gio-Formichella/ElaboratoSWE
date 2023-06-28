package test.daos;
import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.BookingDAO;
import main.orm.ConnectionManager;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookingDAOTest {

    @Test
    public void insert() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(123, false, visit, visitor);

        try {
            dao.insert(b);

            Booking retrieved = dao.get(b.getCode());
            assertEquals(retrieved.getCode(), b.getCode());
            assertEquals(retrieved.getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.isPaid(), b.isPaid());


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.delete(b);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void delete(){
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(128, false, visit, visitor);
        try {
            dao.insert(b);
            dao.delete(b);
            assertNull(dao.get(b.getCode()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Connection con = ConnectionManager.getConnection();

                String sql = "DELETE FROM Booking WHERE code = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, b.getCode());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBooking() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(121, false, visit, visitor);
        Booking b2 = new Booking(189, false, visit, visitor);
        try {
            dao.insert(b);
            dao.insert(b2);
            ArrayList<Booking> retrieved = dao.getBooking(visitor);
            assertEquals(retrieved.get(0).getCode(), b2.getCode());
            assertEquals(retrieved.get(0).getVisit().getCode(), b2.getVisit().getCode());
            assertEquals(retrieved.get(0).getVisitor().getEmailAddress(), b2.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(0).isPaid(), b2.isPaid());
            assertEquals(retrieved.get(1).getCode(), b.getCode());
            assertEquals(retrieved.get(1).getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.get(1).getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(1).isPaid(), b.isPaid());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.delete(b);
                dao.delete(b2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

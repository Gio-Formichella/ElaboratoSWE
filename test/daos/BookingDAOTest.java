package test.daos;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.BookingDAO;
import main.orm.ConnectionManager;
import main.orm.VisitorDAO;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest {

    @Test
    public void delete() throws SQLException {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 128;
        try {
            vdao.insert(visitor);
            dao.addBooking(visit, visitor, b_code, 5);
            dao.delete(b_code);
            assertNull(dao.get(b_code));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            vdao.delete(visitor.getEmailAddress());
        }
    }

    @Test
    public void getBookingVisitor() {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 121;
        try {
            vdao.insert(visitor);
            dao.addBooking(visit, visitor, b_code, 5);
            ArrayList<Booking> retrieved = dao.getVisitorBookings(visitor);
            assertEquals(retrieved.get(0).getCode(), b_code);
            assertEquals(retrieved.get(0).getVisit().getCode(), visit.getCode());
            assertEquals(retrieved.get(0).getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertFalse(retrieved.get(0).isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void setPaid() throws SQLException {
        BookingDAO bdao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 177;

        try {
            vdao.insert(visitor);
            bdao.addBooking(visit, visitor, b_code, 4);
            bdao.setPaid(b_code);
            Booking booking = bdao.get(b_code);
            assertTrue(booking.isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bdao.delete(b_code);
            vdao.delete(visitor.getEmailAddress());
        }
    }

    @Test
    public void addBooking() {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 190;
        Booking retrieved;

        try {
            vdao.insert(visitor);
            dao.addBooking(visit, visitor, b_code, 5);

            retrieved = dao.get(b_code);
            assertEquals(retrieved.getCode(), b_code);
            assertEquals(retrieved.getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertEquals(retrieved.getVisit().getCode(), visit.getCode());
            assertFalse(retrieved.isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBookingVisit() {
        BookingDAO bdao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 165;
        ArrayList<Object> info_booking;
        Booking b;
        Visit v;
        Visitor vr;
        try {
            vdao.insert(visitor);
            bdao.addBooking(visit, visitor, b_code, 5);
            info_booking = bdao.getBookingVisit(b_code);
            b = (Booking) info_booking.get(0);
            v = (Visit) info_booking.get(1);
            vr = (Visitor) info_booking.get(2);
            assertEquals(b_code, b.getCode());
            assertEquals(visit.getCode(), v.getCode());
            assertEquals(visitor.getEmailAddress(), vr.getEmailAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bdao.delete(b_code);
            vdao.delete(visitor.getEmailAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

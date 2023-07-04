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

import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest {
    public void insert(Booking b) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Booking (code, paid, number_of_tickets, visitor, visit) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());
        ps.setInt(3, b.getNumber_of_tickets());
        ps.setString(4, b.getVisitor().getEmailAddress());
        ps.setInt(5, b.getVisit().getCode());


        ps.executeUpdate();
        ps.close();
    }

    @Test
    public void insert() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(129, false, visit, visitor, 5);
        Booking retrieved;

        try {
            insert(b);

            retrieved = dao.get(b.getCode());
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
                dao.delete(b.getCode());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void delete() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(128, false, visit, visitor, 5);
        try {
            insert(b);
            dao.delete(b.getCode());
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
    public void getBookingVisitor() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(121, false, visit, visitor, 5);
        try {
            insert(b);
            ArrayList<Booking> retrieved = dao.getVisitorBookings(visitor);
            assertEquals(retrieved.get(retrieved.size() - 2).getCode(), b.getCode());
            assertEquals(retrieved.get(retrieved.size() - 2).getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.get(retrieved.size() - 2).getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(retrieved.size() - 2).isPaid(), b.isPaid());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.delete(b.getCode());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void setPaid() throws SQLException {
        BookingDAO bdao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(177, false, visit, visitor, 5);

        try {
            insert(b);
            bdao.setPaid(b.getCode());
            Booking booking = bdao.get(b.getCode());
            assertTrue(booking.isPaid());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            bdao.delete(b.getCode());
        }
    }

    @Test
    public void addBooking() {
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        int b_code = 190;
        Booking retrieved;

        try {
            dao.addBooking(visit, visitor, b_code, 5);

            retrieved = dao.get(b_code);
            assertEquals(retrieved.getCode(), b_code);
            assertEquals(retrieved.getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertEquals(retrieved.getVisit().getCode(), visit.getCode());
            assertFalse(retrieved.isPaid());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.delete(b_code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBookingVisit() {
        BookingDAO bdao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200, itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        int b_code = 165;
        ArrayList<Object> info_booking;
        Booking b;
        Visit v;
        Visitor vr;
        try {
            bdao.addBooking(visit, visitor, b_code, 5);
            info_booking = bdao.getBookingVisit(b_code, visit);
            b = (Booking) info_booking.get(0);
            v = (Visit) info_booking.get(1);
            vr = (Visitor) info_booking.get(2);
            assertEquals(b_code, b.getCode());
            assertEquals(visit.getCode(), v.getCode());
            assertEquals(visitor.getEmailAddress(), vr.getEmailAddress());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            bdao.delete(b_code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

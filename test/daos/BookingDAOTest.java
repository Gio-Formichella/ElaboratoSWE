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

        String sql = "INSERT INTO Booking (code, paid, number_of_tickets) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());
        ps.setInt(3, b.getNumber_of_tickets());


        ps.executeUpdate();
        ps.close();

        String sql1 = "INSERT INTO Booking_Visitor (booking, visitor) VALUES (?, ?)";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, b.getCode());
        ps1.setString(2, b.getVisitor().getEmailAddress());

        ps1.executeUpdate();
        ps1.close();

        String sql2 = "INSERT INTO Visit_Booking (visit, booking) VALUES (?, ?)";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, b.getVisit().getCode());
        ps2.setInt(2, b.getCode());

        ps2.executeUpdate();
        ps2.close();
    }
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
        Booking b = new Booking(129, false, visit, visitor, 5);
        ArrayList<Booking> retrieved = new ArrayList<>();

        try {
            insert(b);

            retrieved = dao.get(b.getCode());
            assertEquals(retrieved.get(retrieved.size()-1).getCode(), b.getCode());
            assertEquals(retrieved.get(retrieved.size()-1).getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(retrieved.size()-1).getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.get(retrieved.size()-1).isPaid(), b.isPaid());
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
        Booking b = new Booking(128, false, visit, visitor, 5);
        try {
            insert(b);
            dao.delete(b.getCode());
            assertTrue(dao.get(b.getCode()).isEmpty());
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
        Booking b = new Booking(121, false, visit, visitor, 5);
        try {
            insert(b);
            ArrayList<Booking> retrieved = dao.getBooking(visitor);
            assertEquals(retrieved.get(retrieved.size()-1).getCode(), b.getCode());
            assertEquals(retrieved.get(retrieved.size()-1).getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.get(retrieved.size()-1).getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(retrieved.size()-1).isPaid(), b.isPaid());
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
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(177, false, visit, visitor, 5);

        try{
            insert(b);
            bdao.setPaid(b.getCode());
            ArrayList<Booking> bookings = bdao.get(b.getCode());
            Booking bTrue = bookings.get(0);
            assertTrue(bTrue.isPaid());
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            bdao.delete(b.getCode());
        }
    }

    @Test
    public void addVisit_Booking(){
        BookingDAO dao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        int b_code = 190;
        ArrayList<Booking> retrieved;

        try {
            dao.addVisit_Booking(visit, visitor,b_code, 5);

            retrieved = dao.get(b_code);
            assertEquals(retrieved.get(0).getCode(), b_code);
            assertEquals(retrieved.get(0).getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertEquals(retrieved.get(0).getVisit().getCode(), visit.getCode());
            assertFalse(retrieved.get(0).isPaid());
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

}

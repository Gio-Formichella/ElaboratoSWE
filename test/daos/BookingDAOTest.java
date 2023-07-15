package test.daos;

import main.DomainModel.*;
import main.orm.*;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest {

    @Test
    public void delete() throws SQLException {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ItineraryDAO idao = new ItineraryDAO();
        VisitDAO vidao = new VisitDAO();
        ArtworkDAO adao = new ArtworkDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(8, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(532, "2021-11-25", "12:54:32", 300, 100, "Italiano", itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 128;
        try {
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            vdao.insert(visitor);
            dao.addBooking(visit, visitor, b_code, 5);
            dao.delete(b_code);
            assertNull(dao.get(b_code));
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBookingVisitor() {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ItineraryDAO idao = new ItineraryDAO();
        VisitDAO vidao = new VisitDAO();
        ArtworkDAO adao = new ArtworkDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(8, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(532, "2021-11-25", "12:54:32", 300, 100, "Italiano", itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 121;
        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            dao.addBooking(visit, visitor, b_code, 5);
            ArrayList<Booking> retrieved = dao.getVisitorBookings(visitor);
            assertEquals(retrieved.get(0).getCode(), b_code);
            assertEquals(retrieved.get(0).getVisit().getCode(), visit.getCode());
            assertEquals(retrieved.get(0).getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertFalse(retrieved.get(0).isPaid());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void setPaid() {
        BookingDAO bdao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ItineraryDAO idao = new ItineraryDAO();
        VisitDAO vidao = new VisitDAO();
        ArtworkDAO adao = new ArtworkDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(8, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(532, "2021-11-25", "12:54:32", 300, 100, "Italiano", itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 177;

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            bdao.addBooking(visit, visitor, b_code, 4);
            bdao.setPaid(b_code);
            Booking booking = bdao.get(b_code);
            assertTrue(booking.isPaid());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                bdao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void addBooking() {
        BookingDAO dao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ItineraryDAO idao = new ItineraryDAO();
        VisitDAO vidao = new VisitDAO();
        ArtworkDAO adao = new ArtworkDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(8, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(532, "2021-11-25", "12:54:32", 300, 100, "Italiano", itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 190;
        Booking retrieved;

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            dao.addBooking(visit, visitor, b_code, 5);

            retrieved = dao.get(b_code);
            assertEquals(retrieved.getCode(), b_code);
            assertEquals(retrieved.getVisitor().getEmailAddress(), visitor.getEmailAddress());
            assertEquals(retrieved.getVisit().getCode(), visit.getCode());
            assertFalse(retrieved.isPaid());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBookingVisit() {
        BookingDAO bdao = new BookingDAO();
        VisitorDAO vdao = new VisitorDAO();
        ItineraryDAO idao = new ItineraryDAO();
        VisitDAO vidao = new VisitDAO();
        ArtworkDAO adao = new ArtworkDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(8, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(532, "2021-11-25", "12:54:32", 300, 100, "Italiano", itineraries);
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        int b_code = 165;
        ArrayList<Object> info_booking;
        Booking b;
        Visit v;
        Visitor vr;
        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            bdao.addBooking(visit, visitor, b_code, 5);
            info_booking = bdao.getBookingVisit(b_code);
            b = (Booking) info_booking.get(0);
            v = (Visit) info_booking.get(1);
            vr = (Visitor) info_booking.get(2);
            assertEquals(b_code, b.getCode());
            assertEquals(visit.getCode(), v.getCode());
            assertEquals(visitor.getEmailAddress(), vr.getEmailAddress());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                bdao.delete(b_code);
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

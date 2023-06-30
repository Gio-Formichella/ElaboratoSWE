package test;

import main.DomainModel.*;
import main.business_logic.VisitorController;
import main.orm.BookingDAO;
import main.orm.VisitorDAO;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
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
            v.cancelBooking(b.getCode());
            assertTrue(bdao.get(b.getCode()).isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void subscribeToNewsletter() throws SQLException {
        Visitor v = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        VisitorController vc = new VisitorController();
        VisitorDAO vdao = new VisitorDAO();
        Visitor vTrue;
        try{
            vc.SubscribeToNewsletter(v);
            vTrue = vdao.get(v.getEmailAddress());
            assertTrue(vTrue.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            vc.UnsubscribeFromNewsletter(v);
        }
    }
    @Test
    public void unsubscribeFromNewsletter() throws SQLException {
        Visitor v = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        VisitorController vc = new VisitorController();
        VisitorDAO vdao = new VisitorDAO();
        Visitor vFalse;
        try{
            vc.SubscribeToNewsletter(v);
            vc.UnsubscribeFromNewsletter(v);
            vFalse = vdao.get(v.getEmailAddress());
            assertFalse(vFalse.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bookVisit() throws SQLException, ParseException {
        VisitorController vc = new VisitorController();
        BookingDAO bdao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        int b_code = 170;

        try{
            vc.bookVisit(visit, visitor, b_code, 5);
            assertEquals(b_code, bdao.get(b_code).get(0).getCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            vc.cancelBooking(b_code);
        }
    }

    @Test
    public void payFee() throws SQLException, ParseException {
        VisitorController vc = new VisitorController();

        BookingDAO bdao = new BookingDAO();
        ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);
        Visit visit = new Visit(485, "2020-01-01", "10:23:45", 120, 200,  itineraries);
        Visitor visitor = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Booking b = new Booking(191, false, visit, visitor, 5);

        try{
            vc.bookVisit(visit, visitor, b.getCode(), 5);
            vc.payFee(b.getCode());
            assertTrue(bdao.get(b.getCode()).get(0).isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            vc.cancelBooking(b.getCode());
        }
    }
}

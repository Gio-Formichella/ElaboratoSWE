package test.controllers;

import main.DomainModel.*;
import main.business_logic.VisitorController;
import main.orm.*;
import org.junit.Test;

import java.sql.Array;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VisitorTest {
    @Test
    public void cancelBooking() {
        VisitorController v = new VisitorController();

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
        Booking b = new Booking(194, false, visit, visitor, 5);

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            v.bookVisit(visit, visitor, b.getCode(), 4);
            v.cancelBooking(b.getCode());
            assertNull(bdao.get(b.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void subscribeToNewsletter(){
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        VisitorController vc = new VisitorController();
        VisitorDAO vdao = new VisitorDAO();
        Visitor vTrue;
        try {
            vdao.insert(visitor);
            vc.SubscribeToNewsletter(visitor);
            vTrue = vdao.get(visitor.getEmailAddress());
            assertTrue(vTrue.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(visitor.getEmailAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void unsubscribeFromNewsletter() {
        Visitor visitor = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        VisitorController vc = new VisitorController();
        VisitorDAO vdao = new VisitorDAO();
        Visitor vFalse;
        try {
            vdao.insert(visitor);
            vc.SubscribeToNewsletter(visitor);
            vc.UnsubscribeFromNewsletter(visitor);
            vFalse = vdao.get(visitor.getEmailAddress());
            assertFalse(vFalse.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(visitor.getEmailAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void bookVisit() {
        VisitorController vc = new VisitorController();
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
        int b_code = 170;

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            vc.bookVisit(visit, visitor, b_code, 5);
            assertEquals(b_code, bdao.get(b_code).getCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vc.cancelBooking(b_code);
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void payFee() {
        VisitorController vc = new VisitorController();

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
        Booking b = new Booking(191, false, visit, visitor, 5);

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            vc.bookVisit(visit, visitor, b.getCode(), 5);
            vc.payFee(b.getCode());
            assertTrue(bdao.get(b.getCode()).isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vc.cancelBooking(b.getCode());
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void viewBookings() {
        VisitorController vc = new VisitorController();

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

        Booking b = new Booking(151, false, visit, visitor, 5);
        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            vc.bookVisit(visit, visitor, b.getCode(), 5);
            ArrayList<Booking> retrieved = vc.viewBookings(visitor);

            assertEquals(retrieved.get(0).getCode(), b.getCode());
            assertEquals(retrieved.get(0).getVisit().getCode(), b.getVisit().getCode());
            assertEquals(retrieved.get(0).getVisitor().getEmailAddress(), b.getVisitor().getEmailAddress());
            assertEquals(retrieved.get(0).isPaid(), b.isPaid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vc.cancelBooking(b.getCode());
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBookedTicketInfo() {
        VisitorController vc = new VisitorController();

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
        Booking b = new Booking(181, false, visit, visitor, 5);
        ArrayList<Object> info_booking;
        Booking b_test;
        Visitor vr_test;
        Visit v_test;

        try {
            vdao.insert(visitor);
            adao.insert(art);
            idao.insert(it);
            vidao.insert(visit);
            vc.bookVisit(visit, visitor, b.getCode(), 5);
            info_booking = vc.getBookedTicketInfo(b.getCode());
            b_test = (Booking) info_booking.get(0);
            v_test = (Visit) info_booking.get(1);
            vr_test = (Visitor) info_booking.get(2);

            assertEquals(b.getCode(), b_test.getCode());
            assertEquals(visit.getCode(), v_test.getCode());
            assertEquals(visitor.getEmailAddress(), vr_test.getEmailAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vc.cancelBooking(b.getCode());
                vdao.delete(visitor.getEmailAddress());
                vidao.delete(visit.getCode());
                idao.delete(it);
                adao.delete(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void viewArtworks() {
        VisitorController vc = new VisitorController();

        ArtworkDAO adao = new ArtworkDAO();
        Artwork a = new Artwork(121, "Gioconda", "Leonardo da Vinci", new OnDisplay());
        ArrayList<Artwork> artworks = new ArrayList<>();
        try {
            adao.insert(a);
            artworks = vc.viewArtworks();
            assertEquals(a.getCode(), artworks.get(artworks.size() - 1).getCode());
            assertEquals(a.getName(), artworks.get(artworks.size() - 1).getName());
            assertEquals(a.getAuthor(), artworks.get(artworks.size() - 1).getAuthor());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                adao.delete(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

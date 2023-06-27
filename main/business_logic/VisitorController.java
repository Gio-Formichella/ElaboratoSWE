package main.business_logic;

import main.DomainModel.Artwork;
import main.DomainModel.Booking;
import main.DomainModel.Itinerary;
import main.DomainModel.Visitor;
import main.orm.ArtworkDAO;
import main.orm.BookingDAO;
import main.orm.ItineraryDAO;
import main.orm.VisitorDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class VisitorController {

    public ArrayList<Booking> viewBookings(Visitor v) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        return bdao.getBooking(v);
    }

    public void cancelBooking(Booking b) throws SQLException{
        BookingDAO dao = new BookingDAO();
        dao.delete(b);
    }

    public ArrayList<Artwork> viewArtworks() throws SQLException{
        ArtworkDAO adao = new ArtworkDAO();
        return adao.getAll();
    }

    public void SubscribeToNewsletter(Visitor v) throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        vdao.setSubscriber(v);
    }

    public void UnsubscribeFromNewsletter(Visitor v) throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        vdao.cancelSubscriber(v);
    }
}

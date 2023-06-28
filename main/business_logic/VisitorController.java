package main.business_logic;

import main.DomainModel.*;
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

    public void cancelBooking(Booking b) throws SQLException, ParseException {
        BookingDAO dao = new BookingDAO();
        if(dao.get(b.getCode()) != null) {
            dao.delete(b);
        }
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

    public void bookVisit(Visit v, Visitor vr) throws SQLException{
        BookingDAO bdao = new BookingDAO();
        bdao.addVisit_Booking(v, vr);
    }

    public String printTicket(int code) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        if(bdao.get(code) != null) {
            return bdao.print(code);
        } else {
            return null;
        }
    }
}

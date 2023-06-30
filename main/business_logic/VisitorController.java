package main.business_logic;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.BookingDAO;
import main.orm.VisitDAO;
import main.orm.VisitorDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class VisitorController {

    public ArrayList<Booking> viewBookings(Visitor v) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        return bdao.getBooking(v);
    }

    public void cancelBooking(int code) throws SQLException, ParseException {
        BookingDAO dao = new BookingDAO();
        if(dao.get(code).isEmpty()) {
            System.out.println("La prenotazione richiesta non è presente");
        } else {
            dao.delete(code);
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

    public void bookVisit(Visit v, Visitor vr, int code, int num_visitors) throws Exception {
        VisitDAO vdao = new VisitDAO();
        BookingDAO bdao = new BookingDAO();
        int booked_tickets = vdao.getBookedTickets(v);
        if(bdao.get(code).isEmpty() && v.getMaxVisitors() >= (booked_tickets + num_visitors) ) {
            bdao.addVisit_Booking(v, vr, code, num_visitors);
        } else {
            throw new Exception("Non ci sono posti liberi");
        }
    }

    public ArrayList<Booking> getBookedTicketInfo(int code) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        if(bdao.get(code) != null) {
            return bdao.get(code);
        } else {
            return null;
        }
    }

    public void payFee(int code) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        if(bdao.get(code) != null) {
            bdao.setPaid(code);
        }
    }
}

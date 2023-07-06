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
        return bdao.getVisitorBookings(v);
    }

    public void cancelBooking(int code) throws SQLException, ParseException {
        BookingDAO dao = new BookingDAO();
        if (dao.get(code) == null) {
            System.out.println("La prenotazione richiesta non è presente");
        } else {
            dao.delete(code);
        }
    }

    public ArrayList<Artwork> viewArtworks() throws SQLException {
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
        if(num_visitors > 0){
            if (bdao.get(code) == null && v.getMaxVisitors() >= (booked_tickets + num_visitors)) {
                bdao.addBooking(v, vr, code, num_visitors);
            } else {
                throw new Exception("Non ci sono posti liberi oppure la prenotazione è già esistente");
            }
        } else {
            throw new Exception("Il numero di visitatori non è corretto");
        }

    }

    public ArrayList<Object> getBookedTicketInfo(int code) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        if (bdao.getBookingVisit(code) != null) {
            return bdao.getBookingVisit(code);
        } else {
            throw new SQLException("Non ci sono prenotazioni alla visita con quel codice");
        }
    }

    public void payFee(int code) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        if (bdao.get(code) != null) {
            bdao.setPaid(code);
        }
    }

    public ArrayList<Visit> viewVisits() throws SQLException, ParseException{
        VisitDAO dao = new VisitDAO();
        return dao.getAll();
    }
}

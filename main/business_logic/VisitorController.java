package main.business_logic;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.BookingDAO;
import main.orm.VisitorDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class VisitorController {

    public ArrayList<Booking> viewBookings(main.DomainModel.Visitor v) throws SQLException, ParseException {
        BookingDAO bdao = new BookingDAO();
        return bdao.getBooking(v);
    }

    public void cancelBooking(Booking b) throws SQLException, ParseException {
        BookingDAO dao = new BookingDAO();
        if(dao.get(b.getCode()) != null) {
            dao.delete(b.getCode());
        }
    }

    public ArrayList<Artwork> viewArtworks() throws SQLException{
        ArtworkDAO adao = new ArtworkDAO();
        return adao.getAll();
    }

    public void SubscribeToNewsletter(main.DomainModel.Visitor v) throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        vdao.setSubscriber(v);
    }

    public void UnsubscribeFromNewsletter(main.DomainModel.Visitor v) throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        vdao.cancelSubscriber(v);
    }

    public void bookVisit(Visit v, Visitor vr, int code, int num_visitors) throws SQLException, ParseException {
        int actual_visitors;
        actual_visitors = v.getNum_visitors();
        BookingDAO bdao = new BookingDAO();
        if(bdao.get(code) == null && v.getMaxVisitors() >= (actual_visitors + num_visitors) ) {
            bdao.addVisit_Booking(v, vr, code);
            v.setNum_visitors(actual_visitors + num_visitors);
        }
    }

    public ArrayList<Booking> getTicketInfo(int code) throws SQLException, ParseException {
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

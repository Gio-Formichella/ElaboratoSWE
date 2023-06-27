package main.business_logic;

import main.DomainModel.Artwork;
import main.DomainModel.Booking;
import main.DomainModel.Visitor;
import main.orm.ArtworkDAO;
import main.orm.VisitorDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class VisitorController {

    public ArrayList<Booking> viewBookings(Visitor v) throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        return vdao.getBooking(v);
    }

}

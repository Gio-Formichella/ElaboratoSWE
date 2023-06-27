package main.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.DomainModel.*;

public class VisitorDAO {
    public ArrayList<Visitor> getNLSubscribers() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        ArrayList<Visitor> nlsubscribers = new ArrayList<>();

        String sql = "SELECT email, name, surname FROM Visitor WHERE newsletter = true";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            nlsubscribers.add(new Visitor(name, surname, email, true));
        }

        return nlsubscribers;
    }

    public ArrayList<Visitor> getToBeNotifiedVisitors(Artwork a) throws SQLException {
        //returns visitors whose visit would include given artwork

        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT email, Visitor.name as name, surname, newsletter FROM artwork_itinerary as at, visit_booking as vb, booking_visitor as bv, visitor WHERE artwork = ? AND at.itinerary = visit_itinerary AND vb.booking = bv.booking and bv.visitor=email";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, a.getCode());
        ResultSet rs = ps.executeQuery();

        ArrayList<Visitor> visitors = new ArrayList<>();
        while (rs.next()) {
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            visitors.add(new Visitor(email, name, surname, subscriber));
        }
        return visitors;
    }


    public ArrayList<Booking> getBooking(Visitor v) throws SQLException{
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT BV.booking as booking, B.paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter" +
                "FROM Booking_Visitor as BV, Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B" +
                "WHERE BV.visitor = VR.email AND VB.visit_code = V.code AND VB.visit_itinerary = V.itinerary AND BV.booking = VB.booking AND BV.visitor = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, v.getEmailAddress());
        ResultSet rs = ps.executeQuery();

        ArrayList<Booking> bookings = new ArrayList<>();
        VisitDAO vDAO = new VisitDAO();
        Visit visit = null;
        while (rs.next()){
            int booking = rs.getInt("booking");
            boolean paid = rs.getBoolean("paid");
            int visit_code = rs.getInt("visit");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            visit = vDAO.getTransitive(visit_code);
            bookings.add(new Booking(booking, paid, visit, visitor));
        }
        return bookings;
    }
}


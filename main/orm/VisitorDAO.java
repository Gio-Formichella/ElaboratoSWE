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

        String sql = """
                SELECT email, name, surname, newsletter
                FROM artwork_itinerary at, visit_itinerary vt, visit_booking vb, booking_visitor bv, visitor v
                WHERE artwork = ? AND at.itinerary = vt.itinerary AND vt.visit = vb.visit AND vb.booking = bv.booking AND bv.visitor = v.email\s""";
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
}

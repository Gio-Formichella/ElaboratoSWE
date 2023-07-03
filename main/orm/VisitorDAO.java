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
                SELECT email, v.name name, surname, newsletter\s
                FROM artwork_itinerary as at, Visit_itinerary vi, Booking b, Visitor v
                WHERE artwork = ? AND at.itinerary = vi.itinerary AND vi.visit = b.visit AND b.visitor = v.email""";
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

    public ArrayList<Visitor> getToBeNotifiedVisitors(Visit v) throws SQLException {
        //returns visitors who booked the modified/cancelled visit

        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT email, Visitor.name as name, surname, newsletter from visitor join booking on visitor=visitor.email join visit on visit.code=booking.visit where visit = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, v.getCode());
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

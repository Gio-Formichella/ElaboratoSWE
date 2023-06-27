package main.orm;

import main.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class BookingDAO {

    public void delete(Booking b) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql3 = "DELETE FROM Booking_Visitor WHERE booking = ?";
        PreparedStatement ps3 = con.prepareStatement(sql3);
        ps3.setInt(1, b.getCode());
        ps3.executeUpdate();
        ps3.close();

        String sql2 = "DELETE FROM Visit_Booking WHERE booking = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, b.getCode());
        ps2.executeUpdate();
        ps2.close();

        String sql1 = "DELETE FROM Booking WHERE code = ?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, b.getCode());
        ps1.executeUpdate();
        ps1.close();
    }

    public ArrayList<Booking> getBooking(Visitor v) throws SQLException, ParseException {
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

    public void insert(Booking b) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Booking (code, paid) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());

        ps.executeUpdate();
        ps.close();
    }

    public Booking get(int code) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter" +
                "FROM Booking_Visitor as BV, Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B" +
                "WHERE BV.visitor = VR.email AND VB.visit_code = V.code AND VB.visit_itinerary = V.itinerary AND BV.booking = VB.booking AND B.code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ResultSet rs = ps.executeQuery();

        Booking b = null;
        VisitDAO vDAO = new VisitDAO();
        Visit visit = null;
        if (rs.next()) {
            boolean paid = rs.getBoolean("paid");
            int visit_code = rs.getInt("visit");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            visit = vDAO.getTransitive(visit_code);
            b = new Booking(code, paid, visit, visitor);
        }
        return b;

    }
}

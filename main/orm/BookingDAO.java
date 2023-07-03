
package main.orm;

import main.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class BookingDAO {

    public void delete(int code) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql1 = "DELETE FROM Booking WHERE code = ?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, code);
        ps1.executeUpdate();
        ps1.close();
    }

    public ArrayList<Booking> getBookingVisitor(Visitor v) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.code as booking, B.paid, B.visit as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visitor as VR, Booking as B  WHERE VR.email = B.visitor  AND B.visitor = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, v.getEmailAddress());
        ResultSet rs = ps.executeQuery();

        ArrayList<Booking> bookings = new ArrayList<>();
        VisitDAO vDAO = new VisitDAO();
        Visit visit;
        while (rs.next()) {
            int booking = rs.getInt("booking");
            boolean paid = rs.getBoolean("paid");
            int visit_code = rs.getInt("visit");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            int num_of_tickets = rs.getInt("number_of_tickets");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            visit = vDAO.getTransitive(visit_code);
            bookings.add(new Booking(booking, paid, visit, visitor, num_of_tickets));
        }
        return bookings;
    }

    public ArrayList<Booking> get(int code) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid as paid, B.visit as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visitor as VR, Booking as B WHERE B.visitor = VR.email AND B.code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ResultSet rs = ps.executeQuery();

        ArrayList<Booking> bookings = new ArrayList<>();
        VisitDAO vDAO = new VisitDAO();
        Visit visit = null;
        Booking b = null;
        while (rs.next()) {
            boolean paid = rs.getBoolean("paid");
            int visit_code = rs.getInt("visit");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            int num_of_tickets = rs.getInt("number_of_tickets");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            visit = vDAO.getTransitive(visit_code);
            b = new Booking(code, paid, visit, visitor, num_of_tickets);
            bookings.add(b);

        }
        return bookings;

    }

    public ArrayList<Object> getBookingVisit(int code, Visit v) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid as paid, B.visit as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visitor as VR, Booking as B WHERE B.visitor = VR.email AND B.code = ? AND B.visit = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ps.setInt(2, v.getCode());
        ResultSet rs = ps.executeQuery();
        ArrayList<Object> info_booking = new ArrayList<>();
        Booking b;
        while (rs.next()) {
            boolean paid = rs.getBoolean("paid");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            int num_of_tickets = rs.getInt("number_of_tickets");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            b = new Booking(code, paid, v, visitor, num_of_tickets);
            info_booking.add(b);
            info_booking.add(v);
            info_booking.add(visitor);
        }
        return info_booking;
    }

    public void addVisit_Booking(Visit v, Visitor vr, int code, int number_of_tickets) throws SQLException {
        Connection con = ConnectionManager.getConnection();


        Booking b = new Booking(code, false, v, vr, number_of_tickets);
        String sql = "INSERT INTO Booking (code, paid, number_of_tickets, visitor, visit) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());
        ps.setInt(3, b.getNumber_of_tickets());
        ps.setString(4, b.getVisitor().getEmailAddress());
        ps.setInt(5, b.getVisit().getCode());

        ps.executeUpdate();
        ps.close();

    }

    public void setPaid(int code) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "UPDATE Booking SET paid = TRUE WHERE code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);

        ps.executeUpdate();
        ps.close();
    }

}

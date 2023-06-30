
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

        String sql2 = "DELETE FROM Visit_Booking WHERE booking = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, code);
        ps2.executeUpdate();
        ps2.close();

        String sql1 = "DELETE FROM Booking WHERE code = ?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, code);
        ps1.executeUpdate();
        ps1.close();
    }

    public ArrayList<Booking> getBookingVisitor(Visitor v) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.code as booking, B.paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B  WHERE VB.visit = V.code AND VR.email = B.visitor AND VB.booking = B.code AND B.visitor = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, v.getEmailAddress());
        ResultSet rs = ps.executeQuery();

        ArrayList<Booking> bookings = new ArrayList<>();
        VisitDAO vDAO = new VisitDAO();
        Visit visit = null;
        /*ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);*/
        while (rs.next()){
            int booking = rs.getInt("booking");
            boolean paid = rs.getBoolean("paid");
            int visit_code = rs.getInt("visit");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean subscriber = rs.getBoolean("newsletter");
            int num_of_tickets = rs.getInt("number_of_tickets");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            //visit = new Visit(visit_code, "2020-01-01", "10:23:45", 120, 200,  itineraries);
            visit = vDAO.getTransitive(visit_code);
            bookings.add(new Booking(booking, paid, visit, visitor, num_of_tickets));
        }
        return bookings;
    }

    public ArrayList<Booking> get(int code) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid as paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B WHERE VB.visit = V.code AND B.visitor = VR.email AND B.code = VB.booking AND B.code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ResultSet rs = ps.executeQuery();

        ArrayList<Booking> bookings = new ArrayList<>();
        VisitDAO vDAO = new VisitDAO();
        Visit visit = null;
        Booking b = null;
        /*ArrayList<Artwork> artworks = new ArrayList<>();
        Artwork art = new Artwork(5, "La passeggiata", "Monet",new OnDisplay());
        artworks.add(art);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary it = new Itinerary(90, "Egitto", artworks);
        itineraries.add(it);*/
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
            //visit = new Visit(visit_code, "2020-01-01", "10:23:45", 120, 200,  itineraries);
            b = new Booking(code, paid, visit, visitor, num_of_tickets);
            bookings.add(b);

        }
        return bookings;

    }

    public ArrayList<Object> getBookingVisit(int code, Visit v) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid as paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_tickets as number_of_tickets FROM Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B WHERE VB.visit = V.code AND B.visitor = VR.email AND B.code = VB.booking AND B.code = ? AND V.code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ps.setInt(2, v.getCode());
        ResultSet rs = ps.executeQuery();
        ArrayList<Object> info_booking = new ArrayList<>();
        Booking b = null;
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
        String sql = "INSERT INTO Booking (code, paid, number_of_tickets, visitor) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());
        ps.setInt(3, b.getNumber_of_tickets());
        ps.setString(4, b.getVisitor().getEmailAddress());

        ps.executeUpdate();
        ps.close();

        String sql2 = "INSERT INTO Visit_Booking (visit, booking) VALUES (?, ?)";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, v.getCode());
        ps2.setInt(2, b.getCode());

        ps2.executeUpdate();
        ps2.close();

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

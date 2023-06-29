
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

        String sql3 = "DELETE FROM Booking_Visitor WHERE booking = ?";
        PreparedStatement ps3 = con.prepareStatement(sql3);
        ps3.setInt(1, code);
        ps3.executeUpdate();
        ps3.close();

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

    public ArrayList<Booking> getBooking(Visitor v) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT BV.booking as booking, B.paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_booking as number_of_booking FROM Booking_Visitor as BV, Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B  WHERE BV.visitor = VR.email AND VB.visit = V.code AND BV.booking = B.code AND VB.booking = B.code AND BV.visitor = ?";
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
            int num_of_booking = rs.getInt("number_of_booking");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            //visit = new Visit(visit_code, "2020-01-01", "10:23:45", 120, 200,  itineraries);
            visit = vDAO.getTransitive(visit_code);
            bookings.add(new Booking(booking, paid, visit, visitor, num_of_booking));
        }
        return bookings;
    }

    public ArrayList<Booking> get(int code) throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT DISTINCT B.paid as paid, V.code as visit, VR.email as email, VR.name as name, VR.surname as surname, VR.newsletter as newsletter, B.number_of_booking as number_of_booking FROM Booking_Visitor as BV, Visit_Booking as VB, Visit as V, Visitor as VR, Booking as B WHERE BV.visitor = VR.email AND VB.visit = V.code AND B.code = BV.booking AND B.code = VB.booking AND B.code = ?";
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
            int num_of_booking = rs.getInt("number_of_booking");
            Visitor visitor = new Visitor(name, surname, email, subscriber);
            visit = vDAO.getTransitive(visit_code);
            //visit = new Visit(visit_code, "2020-01-01", "10:23:45", 120, 200,  itineraries);
            b = new Booking(code, paid, visit, visitor, num_of_booking);
            bookings.add(b);

        }
        return bookings;

    }

    public void addVisit_Booking(Visit v, Visitor vr, int code, int number_of_booking) throws SQLException {
        Connection con = ConnectionManager.getConnection();


        Booking b = new Booking(code, false, v, vr, number_of_booking);
        String sql = "INSERT INTO Booking (code, paid, number_of_booking) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, b.getCode());
        ps.setBoolean(2, b.isPaid());
        ps.setInt(3, b.getNumber_of_booking());

        ps.executeUpdate();
        ps.close();

        String sql1 = "INSERT INTO Booking_Visitor (booking, visitor) VALUES (?, ?)";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, b.getCode());
        ps1.setString(2, vr.getEmailAddress());

        ps1.executeUpdate();
        ps1.close();

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

    public int getBookedTickets(Visit v) throws SQLException {
        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT SUM(number_of_booking) as number_of_bookings FROM Visit as V, Visit_Booking as VB, Booking as B WHERE B.code = VB.booking AND V.code = VB.Visit AND V.code = ? GROUP BY V.code";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, v.getCode());
        ResultSet rs = ps.executeQuery();
        int num_of_bookings;

        if(rs.next()){
            num_of_bookings = rs.getInt("number_of_bookings");
        } else {
            num_of_bookings = 0;
        }

        return num_of_bookings;
    }
}

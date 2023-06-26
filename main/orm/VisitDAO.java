package main.orm;

import main.DomainModel.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class VisitDAO {
    
    public Visit getTransitive(int code) throws SQLException, ParseException { //also instantiates Itineraries
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT code, date_, time_, max_visitors, price, itinerary FROM Visit WHERE code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ResultSet rs = ps.executeQuery();
        Visit v = null;
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        ItineraryDAO iDAO = new ItineraryDAO();
        if(rs.next()){
            java.util.Date date = inputFormat.parse(rs.getDate("date_").toString());
            String formattedDate = outputFormat.format(date);
            v = new Visit(rs.getInt("code"), formattedDate, rs.getString("time_"), rs.getInt("max_visitors"), rs.getFloat("price"), itineraries);
            do {//uses ItineraryDAO to instantiate Itinerary objects
            Itinerary i = iDAO.getTransitive(rs.getInt("itinerary"));
            v.addItinerary(i);  
            }while (rs.next());
            
        }else{
            return null;
        }
        
        return v;
    }

    public void insert(Visit v) throws SQLException, ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO Visit (code, date_, time_, max_visitors, price, itinerary) VALUES ( ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ArrayList<Itinerary> itineraries = v.getItineraries();
        java.util.Date utilDate = format.parse(v.getDate());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(v.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        ps.setInt(1, v.getCode());
        ps.setDate(2, sqlDate);
        ps.setTime(3, java.sql.Time.valueOf(localTime));
        ps.setInt(4, v.getMaxVisitors());
        ps.setFloat(5, v.getPrice());
        if(itineraries.isEmpty()) {
            throw new SQLException("can't create a visit with no itineraries");
        }else{
            for (Itinerary i : itineraries) {//instantiates tuples for every itinerary
                ps.setInt(6, i.getId());
                ps.executeUpdate();
            }
        }
        ps.close();
    }

    public void delete(int code) throws SQLException {
        Connection con = ConnectionManager.getConnection();

        String sql = "DELETE FROM Visit WHERE code = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, code);
        ps.executeUpdate();
        ps.close();
    }

    public void update(Visit v) throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        Connection con = ConnectionManager.getConnection();

        String sql = "UPDATE Visit SET date_ = ?, time_ = ?, max_visitors = ?, price = ? WHERE code = ?";
        java.util.Date utilDate = format.parse(v.getDate());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        LocalTime localTime = LocalTime.parse(v.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, sqlDate );
        ps.setTime(2, java.sql.Time.valueOf(localTime));
        ps.setInt(3, v.getMaxVisitors());
        ps.setFloat(4, v.getPrice());
        ps.setInt(5, v.getCode());
        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<Visit> getAll() throws SQLException, ParseException {
        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT code FROM Visit GROUP BY code";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Visit> visits = new ArrayList<>();
        while (rs.next()) {
            Visit v = getTransitive(rs.getInt("code"));
            visits.add(v);
        }
        return visits;
    }

    public void removeItineraryFromVisits(int id)throws SQLException{
        Connection con = ConnectionManager.getConnection();

        String sql = "DELETE FROM Visit WHERE itinerary = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }
}
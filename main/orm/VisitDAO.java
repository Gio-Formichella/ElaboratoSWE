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

}

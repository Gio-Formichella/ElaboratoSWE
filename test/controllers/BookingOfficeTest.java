package test.controllers;

import main.DomainModel.Itinerary;
import main.DomainModel.Visit;
import main.business_logic.BookingOffice;
import main.orm.ItineraryDAO;
import main.orm.VisitDAO;
import org.junit.Test;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookingOfficeTest {
    

    @Test
    public void setVisit(){        
        BookingOffice b = new BookingOffice();
        int code = 1;
        String date = "2020-01-01";
        String time = "10:00:00";
        int maxVisitors = 100;
        float price = 10;
        String language = "Italiano";
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i = new Itinerary(1, "itinerary1", null);
        itineraries.add(i);
        
        try {
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.insert(i);
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
            VisitDAO vDao = new VisitDAO();
            Visit v = vDao.getTransitive(code);
            assertEquals(v.getCode(), code);
            assertEquals(v.getDate(), date);
            assertEquals(v.getTime(), time);
            assertEquals(v.getMaxVisitors(), maxVisitors);
            assertEquals(v.getPrice(), price);
            assertFalse(v.getItineraries().isEmpty());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally{
            try {
                VisitDAO vDao = new VisitDAO();
                vDao.delete(code);
                ItineraryDAO iDao = new ItineraryDAO();
                iDao.delete(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void cancelVisit(){
        BookingOffice b = new BookingOffice();
        int code = 1;
        String date = "2020-01-01";
        String time = "10:00:00";
        int maxVisitors = 100;
        float price = 10;
        String language = "Italiano";
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i = new Itinerary(1, "itinerary1", null);
        itineraries.add(i);
        try {
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.insert(i);
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
            b.cancelVisit(code);
            VisitDAO dao = new VisitDAO();
            assertNull(dao.getTransitive(code));
        } catch (SQLException | ParseException | MessagingException e) {
            e.printStackTrace();
        } finally{
            try {
                VisitDAO dao = new VisitDAO();
                dao.delete(code);
                ItineraryDAO iDao = new ItineraryDAO();
                iDao.delete(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void modifyVisit(){
        BookingOffice b = new BookingOffice();
        int code = 1;
        String date = "2020-01-01";
        String time = "10:00:00";
        int maxVisitors = 100;
        String language = "Italiano";
        float price = 10;
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i = new Itinerary(1, "itinerary1", null);
        itineraries.add(i);
        try {
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.insert(i);
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
            String date2 = "2020-01-31";
            String time2 = "11:00:00";
            int maxVisitors2 = 200;
            String language2 = "Inglese";
            Visit v = new Visit(code, date2, time2, maxVisitors2, price, language2, itineraries);
            b.modifyVisit(v);
            VisitDAO dao = new VisitDAO();
            Visit v2 = dao.getTransitive(code);
            assertEquals(v2.getDate(), date2);
            assertEquals(v2.getTime(), time2);
            assertEquals(v2.getMaxVisitors(), maxVisitors2);
            assertEquals(v2.getPrice(), price);
            assertEquals(v2.getLanguage(), language2);
        } catch (SQLException | ParseException | MessagingException e) {
            e.printStackTrace();
        } finally{
            try {
                VisitDAO dao = new VisitDAO();
                dao.delete(code);
                ItineraryDAO iDao = new ItineraryDAO();
                iDao.delete(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

package test.controllers;

import main.DomainModel.Itinerary;
import main.DomainModel.Visit;
import main.DomainModel.Visitor;
import main.business_logic.BookingOffice;
import main.business_logic.VisitorController;
import main.orm.ItineraryDAO;
import main.orm.VisitDAO;
import main.orm.VisitorDAO;
import main.orm.BookingDAO;
import org.junit.Test;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookingOfficeTest {


    @Test
    public void setVisit() {
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
        } finally {
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
    public void setWrongMaxVisitors() {
        BookingOffice b = new BookingOffice();
        int code = 1;
        String date = "2020-01-01";
        String time = "10:00:00";
        int maxVisitors = 0;
        float price = 10;
        String language = "Italiano";
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i = new Itinerary(1, "itinerary1", null);
        itineraries.add(i);
        assertThrows(SQLException.class, () -> {
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
        });
        try {
            VisitDAO vDao = new VisitDAO();
            vDao.delete(code);
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.delete(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setWrongPrice() {
        BookingOffice b = new BookingOffice();
        int code = 1;
        String date = "2020-01-01";
        String time = "10:00:00";
        int maxVisitors = 100;
        float price = -5;
        String language = "Italiano";
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i = new Itinerary(1, "itinerary1", null);
        itineraries.add(i);
        assertThrows(SQLException.class, () -> {
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
        });
        try {
            VisitDAO vDao = new VisitDAO();
            vDao.delete(code);
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.delete(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelVisit() {
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
            VisitDAO dao = new VisitDAO();
            VisitorDAO vDao = new VisitorDAO();
            Visitor v = vDao.get("michael.bartoloni@stud.unifi.it");
            iDao.insert(i);
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
            VisitorController vr = new VisitorController();
            vr.bookVisit(dao.getTransitive(code), v, 1, 100);
            b.cancelVisit(code);
            assertNull(dao.getTransitive(code));
            BookingDAO bDao = new BookingDAO();
            assertNull(bDao.get(code));
        } catch (SQLException | ParseException | MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                BookingDAO bDao = new BookingDAO();
                bDao.delete(1);
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
    public void modifyVisit() {
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
            VisitDAO dao = new VisitDAO();
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.insert(i);
            dao.insert(new Visit(code, date, time, maxVisitors, price, language, itineraries));
            VisitorDAO vDao = new VisitorDAO();
            Visitor vr = vDao.get("michael.bartoloni@stud.unifi.it");
            BookingDAO bDao = new BookingDAO();
            bDao.addBooking(dao.getTransitive(code), vr, 1, 100);
            String date2 = "2020-01-31";
            String time2 = "11:00:00";
            int maxVisitors2 = 200;
            String language2 = "Inglese";
            Visit v = new Visit(code, date2, time2, maxVisitors2, price, language2, itineraries);
            b.modifyVisit(v);
            Visit v2 = dao.getTransitive(code);
            assertEquals(v2.getDate(), date2);
            assertEquals(v2.getTime(), time2);
            assertEquals(v2.getMaxVisitors(), maxVisitors2);
            assertEquals(v2.getPrice(), price);
            assertEquals(v2.getLanguage(), language2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                VisitDAO dao = new VisitDAO();
                dao.delete(code);
                ItineraryDAO iDao = new ItineraryDAO();
                iDao.delete(i);
                BookingDAO bDao = new BookingDAO();
                bDao.delete(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void modifyVisitWrong() {
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
            VisitDAO dao = new VisitDAO();
            ItineraryDAO iDao = new ItineraryDAO();
            iDao.insert(i);
            VisitorDAO vDao = new VisitorDAO();
            Visitor vr = vDao.get("michael.bartoloni@stud.unifi.it");
            VisitorController vc = new VisitorController();
            b.setVisit(code, date, time, maxVisitors, price, language, itineraries);
            vc.bookVisit(dao.getTransitive(code), vr, 1, 100);
            String date2 = "2020-01-31";
            String time2 = "11:00:00";
            int maxVisitors2 = 0;
            String language2 = "Inglese";
            Visit v = new Visit(code, date2, time2, maxVisitors2, price, language2, itineraries);
            assertThrows(SQLException.class, () -> {
                b.modifyVisit(v);
            });
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
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

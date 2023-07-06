package test.daos;

import main.DomainModel.*;
import main.orm.BookingDAO;
import main.orm.ConnectionManager;
import main.orm.VisitorDAO;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VisitorDAOTest {

    @Test
    void getNLSubscribers() {
        VisitorDAO dao = new VisitorDAO();
        try {
            assertEquals(dao.getNLSubscribers().getClass(), ArrayList.class);
            for (Visitor v : dao.getNLSubscribers()) {
                assertTrue(v.isNLSubscriber());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getToBeNotifiedVisitors() {
        VisitorDAO dao = new VisitorDAO();
        try {
            assertEquals(dao.getNLSubscribers().getClass(), ArrayList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetToBeNotifiedVisitors() {
        VisitorDAO dao = new VisitorDAO();
        try {
            assertEquals(dao.getNLSubscribers().getClass(), ArrayList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void setSubscriber() throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        Visitor visitorFalse = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Visitor visitorTrue = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", true);

        try{
            vdao.setSubscriber(visitorFalse);
            Visitor v = vdao.get(visitorFalse.getEmailAddress());
            assertEquals(v.isNLSubscriber(), visitorTrue.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.cancelSubscriber(visitorFalse);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Test
    public void cancelSubscriber() throws SQLException {
        VisitorDAO vdao = new VisitorDAO();
        Visitor visitorFalse = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        Visitor visitorFalse2 = new Visitor("Davide", "Lombardi", "davide.lombardi2@stud.unifi.it", false);
        try{
            vdao.setSubscriber(visitorFalse);
            vdao.cancelSubscriber(visitorFalse);
            Visitor v = vdao.get(visitorFalse.getEmailAddress());
            assertEquals(v.isNLSubscriber(), visitorFalse2.isNLSubscriber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void insert() {
        VisitorDAO vdao = new VisitorDAO();
        Visitor v = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);
        Visitor visitor;
        try {
            vdao.insert(v);

            visitor = vdao.get(v.getEmailAddress());
            assertEquals(v.getEmailAddress(), visitor.getEmailAddress());
            assertEquals(v.getName(), visitor.getName());
            assertEquals(v.getSurname(), visitor.getSurname());
            assertEquals(v.isNLSubscriber(), visitor.isNLSubscriber());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                vdao.delete(v.getEmailAddress());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void delete() {
        VisitorDAO vdao = new VisitorDAO();
        Visitor v = new Visitor("Mattia", "Baroncelli", "mattia.baroncelli@stud.unifi.it", false);

        try {
            vdao.insert(v);
            vdao.delete(v.getEmailAddress());
            assertNull(vdao.get(v.getEmailAddress()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package test.daos;

import main.DomainModel.Visitor;
import main.orm.VisitorDAO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            vdao.cancelSubscriber(visitorFalse);
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
}

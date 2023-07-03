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
}
package test.daos;

import main.DomainModel.Visitor;
import main.orm.VisitorDAO;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitorDAOTest {
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

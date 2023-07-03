package test.daos;

import main.DomainModel.Visit;
import main.DomainModel.Itinerary;
import main.orm.VisitDAO;
import main.orm.ConnectionManager;
import main.orm.ItineraryDAO;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class VisitDAOTest {
    
    @Test
    void insert(){
        ItineraryDAO iDao = new ItineraryDAO();
        Itinerary i = new Itinerary(89, "Egitto", new ArrayList<>());

        ArrayList<Itinerary> itineraries = new ArrayList<>();
        itineraries.add(i);
        Visit v = new Visit(89, "2020-12-12", "12:00:00", 100, 10.0f, itineraries);
        VisitDAO dao = new VisitDAO();
        try{
            iDao.insert(i);
            dao.insert(v);
        
            // retrieving visit
            Visit retrieved = dao.getTransitive(v.getCode());
            assertEquals(retrieved.getCode(), v.getCode());
            assertEquals(retrieved.getDate(), v.getDate());
            assertEquals(retrieved.getTime(), v.getTime());
            assertEquals(retrieved.getMaxVisitors(), v.getMaxVisitors());
            assertEquals(retrieved.getPrice(), v.getPrice());
            assertTrue(v.getItineraries().size() == retrieved.getItineraries().size());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }finally{ //removing inserted tuple
            try{
                dao.delete(v.getCode());
                iDao.delete(i);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void delete(){
        VisitDAO dao = new VisitDAO();
        Visit v = new Visit(2, "2020-12-12", "12:00:00", 100, 10.0f, new ArrayList<>());
        Itinerary i = new Itinerary(2, "Egitto", new ArrayList<>());
        v.addItinerary(i);
        ItineraryDAO iDao = new ItineraryDAO();

        try{
            iDao.insert(i);
            dao.insert(v);
            dao.delete(v.getCode());
            assertNull(dao.getTransitive(v.getCode()));
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }finally{
            try{  //if something went wrong with delete method, manual deletion of inserted tuple
                Connection con = ConnectionManager.getConnection();
                String sql1 = "DELETE FROM Visit WHERE code = ?";
                String sql2 = "DELETE FROM Itinerary WHERE id = ?";
                PreparedStatement ps1 = con.prepareStatement(sql1);
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, i.getId());
                ps2.executeUpdate();
                ps1.setInt(1, v.getCode());
                ps1.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void update(){
        VisitDAO dao = new VisitDAO();
        Visit v = new Visit(2, "2020-12-12", "12:00:00", 100, 10.0f, new ArrayList<>());
        Itinerary i = new Itinerary(2, "Egitto", new ArrayList<>());
        v.addItinerary(i);
        ItineraryDAO iDao = new ItineraryDAO();
        try{
            iDao.insert(i);
            dao.insert(v);
            Visit v2=new Visit(2, "2020-12-15", "15:00:00", 200, 20.0f, new ArrayList<>());
            dao.update(v2);
            Visit retrieved = dao.getTransitive(v.getCode());
            assertEquals(retrieved.getDate(), v2.getDate());
            assertEquals(retrieved.getTime(), v2.getTime());
            assertEquals(retrieved.getMaxVisitors(), v2.getMaxVisitors());
            assertEquals(retrieved.getPrice(), v2.getPrice());
            dao.delete(v2.getCode());
            assertNull(dao.getTransitive(v2.getCode()));
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }finally{
            try{
                iDao.delete(i);
                dao.delete(v.getCode());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void getTransitive(){
        VisitDAO vDao = new VisitDAO();
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        Itinerary i1=new Itinerary(1, "itinerary1", null);
        Itinerary i2=new Itinerary(2, "itinerary2", null);
        itineraries.add(i1);
        itineraries.add(i2);
        Visit v = new Visit(2, "2020-12-12", "12:00:00", 100, 10.0f, itineraries);
        ItineraryDAO iDao = new ItineraryDAO();
        try{
            iDao.insert(i1);
            iDao.insert(i2);
            vDao.insert(v);
            Visit retrieved = vDao.getTransitive(v.getCode());
            assertEquals(retrieved.getCode(), v.getCode());
            assertEquals(retrieved.getDate(), v.getDate());
            assertEquals(retrieved.getTime(), v.getTime());
            assertEquals(retrieved.getMaxVisitors(), v.getMaxVisitors());
            assertEquals(retrieved.getPrice(), v.getPrice());
            assertTrue(v.getItineraries().size() == retrieved.getItineraries().size());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }finally{
            try{
                vDao.delete(v.getCode());
                iDao.delete(i1);
                iDao.delete(i2);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void removeItineraryFromVisits(){
        VisitDAO vDao = new VisitDAO();
        ItineraryDAO iDao = new ItineraryDAO();
        Itinerary i1=new Itinerary(1, "itinerary1", null);
        Itinerary i2=new Itinerary(2, "itinerary2", null);
        ArrayList<Itinerary> itineraries = new ArrayList<>();
        itineraries.add(i1);
        itineraries.add(i2);
        Visit v = new Visit(2, "2020-12-12", "12:00:00", 100, 10.0f, itineraries);
        try{
            iDao.insert(i1);
            iDao.insert(i2);
            vDao.insert(v);
            vDao.removeItineraryFromVisits(i1.getId());
            Visit retrieved = vDao.getTransitive(v.getCode());
            assertEquals(retrieved.getCode(), v.getCode());
            assertEquals(retrieved.getDate(), v.getDate());
            assertEquals(retrieved.getTime(), v.getTime());
            assertEquals(retrieved.getMaxVisitors(), v.getMaxVisitors());
            assertEquals(retrieved.getPrice(), v.getPrice());
            assertTrue(v.getItineraries().size() > retrieved.getItineraries().size());
            assertFalse(retrieved.getItineraries().contains(i2));
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }finally{
            try{
                vDao.delete(v.getCode());
                iDao.delete(i1);
                iDao.delete(i2);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}

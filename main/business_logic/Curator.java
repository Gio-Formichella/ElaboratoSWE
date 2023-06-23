package main.business_logic;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.ItineraryDAO;
import main.orm.VisitorDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


public class Curator {
    private final String emailAddress = "museroswe@gmail.com";

    public void addItinerary(int id, String name) throws SQLException {
        Itinerary i = new Itinerary(id, name, new ArrayList<>());
        ItineraryDAO dao = new ItineraryDAO();
        dao.insert(i);
    }

    public void addArtwork(Itinerary i, Artwork a) throws SQLException{
        //se opera non ancora presente nel db viene memorizzata
        ArtworkDAO adao = new ArtworkDAO();
        if(adao.get(a.getCode()) == null){
            adao.insert(a);
        }
        ItineraryDAO idao = new ItineraryDAO();
        idao.addArtworkToItinerary(i, a);
        i.addArtwork(a);

        //notifica dei newsletter subscribers
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> nlsubscribers = vdao.getNLSubscribers();
        //TODO metodo di invio email
    }

    public ArrayList<Artwork> viewArtworks() throws SQLException{
        ArtworkDAO adao = new ArtworkDAO();
        return adao.getAll();
    }

    public void modifyStatus(Artwork a, ArtworkStatus as) throws SQLException{
        //se lo stato è lo stesso, nessun cambiamento
        //se lo stato da visibile passa a non visibile, notificati i visitatori interessati dalla modifica
        //se lo stato da non visibile passa a visibile notificati i newsletter subscriber
        if(!Objects.equals(as.getStatus(), a.getStatus())){
            if(a.getArtworkStatusObject().getClass() == OnDisplay.class){
                //opera passa ad uno stato non visibile
                //TODO metodo transitive che fornisce visitatori interessati
                ArrayList<Visitor> visitors = null;
                //TODO metodo di email ai visitatori
            }
            else if(as.getClass() == OnDisplay.class){
                //opera torna allo stato visibile
                VisitorDAO dao = new VisitorDAO();
                ArrayList<Visitor> nlsubscribers = dao.getNLSubscribers();
                //TODO metodo di invio email
            }
            a.setStatus(as);
            ArtworkDAO dao = new ArtworkDAO();
            dao.updateStatus(a);
        }
    }

    public void cancelItinerary(Itinerary i) throws SQLException{
        ItineraryDAO dao = new ItineraryDAO();
        dao.delete(i);
    }

}

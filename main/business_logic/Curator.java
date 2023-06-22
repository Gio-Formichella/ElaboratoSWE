package main.business_logic;

import main.DomainModel.*;
import main.orm.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Curator {
    private final String emailAddress = null; //TODO va aggiunta un email museale

    public void addItinerary(int id, String name) throws SQLException {
        Itinerary i = new Itinerary(id, name, new ArrayList<>());
        ItineraryDAO dao = new ItineraryDAO();
        dao.insert(i);
    }

    public void addArtwork(Itinerary i, Artwork a){
        //TODO verifica se opera già esiste nel db, se non ancora esistente la crea
        //TODO associa opera a itinerario
        //TODO notifica dei newsletter subscribers
    }

    public List<Artwork> viewArtworks(){
        return null;
    }

    public void modifyStatus(Artwork a, ArtworkStatus as){}

    public void cancelItinerary(){}

}

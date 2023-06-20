package main.DomainModel;

import java.util.ArrayList;

public class Itinerary {
    private int id;

    private String name;
    private ArrayList<Artwork> artworks;

    
    public Itinerary(int id, String name, ArrayList<Artwork> artworks) {
        this.id = id;
        this.name = name;
        this.artworks = artworks;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Artwork> getArtworks() {
        return artworks;
    }
}

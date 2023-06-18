package main.DomainModel;

import java.util.ArrayList;

public class Itinerary {
    private int id;
    private ArrayList<Artwork<ArtworkStatus>> artworks;

    
    public Itinerary(int id, ArrayList<Artwork<ArtworkStatus>> artworks) {
        this.id = id;
        this.artworks = artworks;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Artwork<ArtworkStatus>> getArtworks() {
        return artworks;
    }
}

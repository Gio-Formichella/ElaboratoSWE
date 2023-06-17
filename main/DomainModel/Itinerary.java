package main.DomainModel;

import java.util.ArrayList;

public class Itinerary {
    private int id;
    private ArrayList<Artwork> artworks;

    
    public Itinerary(int id, ArrayList<Artwork> artworks) {
        this.id = id;
        this.artworks = artworks;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Artwork> getArtworks() {
        return artworks;
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }

    public void removeArtwork(Artwork artwork) {
        artworks.remove(artwork);
    }
}

package main.DomainModel;

import java.util.Objects;

public class Artwork {
    private int code;
    private String name;
    private String author;
    private ArtworkStatus status;

    public Artwork(int code, String name, String author, ArtworkStatus status) {
        this.code = code;
        this.name = name;
        this.author = author;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status.getStatus();
    }

    public ArtworkStatus getArtworkStatusObject(){
        return status.copy();
    }

    public boolean setStatus(ArtworkStatus as){
        if(!Objects.equals(status.getStatus(), as.getStatus())){
            status = as;
            return true;
        }
        return false; //returns false if new status is the same to the old one
    }
}
package main.DomainModel;

public interface ArtworkStatus {

    String getStatus();

    //defensive copy method
    ArtworkStatus copy();
}
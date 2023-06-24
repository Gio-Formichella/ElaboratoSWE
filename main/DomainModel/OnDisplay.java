package main.DomainModel;

public class OnDisplay implements ArtworkStatus{
    
    public String getStatus() {
        return "On Display";
    }

    @Override
    public OnDisplay copy() {
        return new OnDisplay();
    }
}

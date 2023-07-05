package main.DomainModel;

import java.util.ArrayList;

public class Visit {
    private int code;
    private String date;
    private String time;
    private int maxVisitors;
    private float price;
    private ArrayList<Itinerary> itineraries;

    public Visit(int code, String date, String time, int maxVisitors, float price, ArrayList<Itinerary> itineraries) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.maxVisitors = maxVisitors;
        this.price = price;
        this.itineraries = itineraries;
    }

    public int getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getMaxVisitors() {
        return maxVisitors;
    }

    public float getPrice() {
        return price;
    }

    public ArrayList<Itinerary> getItineraries() {
        ArrayList<Itinerary> itinerariesCopy = new ArrayList<>();
        for (Itinerary i : itineraries) {
            itinerariesCopy.add(i);
        }
        return itinerariesCopy;
    }

    public void addItinerary(Itinerary i){
        itineraries.add(i);
    }
}
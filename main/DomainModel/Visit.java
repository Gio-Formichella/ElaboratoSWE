package main.DomainModel;

import java.util.ArrayList;

public class Visit {
    private String code;
    private String date;
    private String time;
    private int maxVisitors;
    private float price;
    private ArrayList<Itinerary> itineraries;

    public Visit(String code, String date, String time, int maxVisitors, float price, ArrayList<Itinerary> itineraries){
        this.code = code;
        this.date = date;
        this.time = time;
        this.maxVisitors = maxVisitors;
        this.price = price;
        this.itineraries = itineraries;
    }

    public String getCode(){
        return code;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public int getMaxVisitors(){
        return maxVisitors;
    }

    public void setMaxVisitors(int maxVisitors){
        this.maxVisitors = maxVisitors;
    }

    public float getPrice(){
        return price;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public ArrayList<Itinerary> getItineraries(){
        return itineraries;
    }

    public void addItinerary(Itinerary itinerary){
        itineraries.add(itinerary);
    }

    public void removeItinerary(Itinerary itinerary){
        itineraries.remove(itinerary);
    }
}

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

    public String getTime(){
        return time;
    }

    public int getMaxVisitors(){
        return maxVisitors;
    }

    public float getPrice(){
        return price;
    }

    public ArrayList<Itinerary> getItineraries(){
        return itineraries;
    }
}

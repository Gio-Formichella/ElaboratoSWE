package main.DomainModel;

import java.util.ArrayList;

public class Visit {
    private int code;
    private String date;
    private String time;
    private int maxVisitors;
    private float price;
    private ArrayList<Itinerary> itineraries;

    private int num_visitors;

    public Visit(int code, String date, String time, int maxVisitors, float price, ArrayList<Itinerary> itineraries){
        this.code = code;
        this.date = date;
        this.time = time;
        this.maxVisitors = maxVisitors;
        this.price = price;
        this.itineraries = itineraries;
        this.num_visitors = 0;
    }

    public int getCode(){
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

    public int getNum_visitors(){
        return num_visitors;
    }

    public void setNum_visitors(int num_visitors){this.num_visitors = num_visitors; }



    public void addItinerary(Itinerary i){
        itineraries.add(i);
    }
}

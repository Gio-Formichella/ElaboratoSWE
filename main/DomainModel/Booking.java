package main.DomainModel;

public class Booking {
    
    private int code;
    private boolean paid;
    private Visit visit;
    private Visitor visitor;

    private int number_of_booking;
    public Booking(int code, boolean paid, Visit visit, Visitor visitor, int number_of_booking) {
        this.code = code;
        this.paid = paid;
        this.visit = visit;
        this.visitor = visitor;
        this.number_of_booking = number_of_booking;
    }

    public int getCode() {
        return code;
    }

    public boolean isPaid() {
        return paid;
    }

    public Visit getVisit() {
        return visit;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setPaid(){
        this.paid = true;
    }

    public void setNumber_of_booking(int number_of_booking){this.number_of_booking = number_of_booking;}

    public int getNumber_of_booking(){return number_of_booking;}
}

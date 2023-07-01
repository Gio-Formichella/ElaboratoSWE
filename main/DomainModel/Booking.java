package main.DomainModel;

public class Booking {
    
    private int code;
    private boolean paid;
    private Visit visit;
    private Visitor visitor;

    public Booking(int code, boolean paid, Visit visit, Visitor visitor) {
        this.code = code;
        this.paid = paid;
        this.visit = visit;
        this.visitor = visitor;
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
}

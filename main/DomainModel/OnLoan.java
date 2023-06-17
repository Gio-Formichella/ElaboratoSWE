package main.DomainModel;

public class OnLoan implements ArtworkStatus{
    
    private String borrowingMuseum;

    public OnLoan(String borrowingMuseum) {
        this.borrowingMuseum = borrowingMuseum;
    }

    public String getBorrowingMuseum() {
        return borrowingMuseum;
    }

    public void setBorrowingMuseum(String borrowingMuseum) {
        this.borrowingMuseum = borrowingMuseum;
    }

    public String getStatus() {
        return "On Loan";
    }

}

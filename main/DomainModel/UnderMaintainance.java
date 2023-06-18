package main.DomainModel;

public class UnderMaintainance implements ArtworkStatus{
    
    private String estimatedCompletion;

    public UnderMaintainance(String estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public UnderMaintainance(UnderMaintainance status) {
        this.estimatedCompletion = status.getEstimatedCompletion();
    }

    public String getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(String estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public String getStatus() {
        return "Under Maintainance until " + estimatedCompletion;
    }

}

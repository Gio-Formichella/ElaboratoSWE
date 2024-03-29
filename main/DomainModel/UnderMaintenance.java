package main.DomainModel;

public class UnderMaintenance implements ArtworkStatus{
    
    private String estimatedCompletion;

    public UnderMaintenance(String estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public UnderMaintenance(UnderMaintenance as){
        this.estimatedCompletion = as.estimatedCompletion;
    }
    @Override
    public UnderMaintenance copy() {
        return new UnderMaintenance(this);
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

package main.DomainModel;

public class Artwork<AS extends ArtworkStatus> {
    private int code;
    private String name;
    private String author;
    private AS status;

    public Artwork(int code, String name, String author, AS status) {
        this.code = code;
        this.name = name;
        this.author = author;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status.getStatus();
    }

    public void setStatus(AS status) {
        this.status = status;
    }
}
package main.business_logic;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.ItineraryDAO;
import main.orm.VisitorDAO;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;


public class Curator {
    private final Notifier notifier;

    public Curator(){
        notifier = Notifier.getInstance();
    }
    public void addItinerary(int id, String name) throws SQLException {
        Itinerary i = new Itinerary(id, name, new ArrayList<>());
        ItineraryDAO dao = new ItineraryDAO();
        dao.insert(i);
    }

    public void addArtwork(Itinerary i, Artwork a) throws SQLException, MessagingException {
        //se opera non ancora presente nel db viene memorizzata
        ArtworkDAO adao = new ArtworkDAO();
        if (adao.get(a.getCode()) == null) {
            adao.insert(a);
        }
        ItineraryDAO idao = new ItineraryDAO();
        idao.addArtworkToItinerary(i, a);
        i.addArtwork(a);

        //notifica dei newsletter subscribers
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> nlsubscribers = vdao.getNLSubscribers();
        //invio email
        if (nlsubscribers.size() > 0) {
            String messageToSend = "New artwork " + a.getName() + " of the author " + a.getAuthor() + " is now " + a.getStatus() + " in the " + i.getName() + " itinerary";
            notifier.sendEmail(nlsubscribers, "New artwork", messageToSend);
        }
    }


    public ArrayList<Artwork> viewArtworks() throws SQLException {
        ArtworkDAO adao = new ArtworkDAO();
        return adao.getAll();
    }

    public void modifyStatus(Artwork a, ArtworkStatus as) throws SQLException, MessagingException {
        //se lo stato è lo stesso, nessun cambiamento
        //se lo stato da visibile passa a non visibile, notificati i visitatori interessati dalla modifica
        //se lo stato da non visibile passa a visibile notificati i newsletter subscriber
        if (!Objects.equals(as.getStatus(), a.getStatus())) {
            if (a.getArtworkStatusObject().getClass() == OnDisplay.class) {
                //opera passa ad uno stato non visibile
                VisitorDAO dao = new VisitorDAO();
                //metodo transitive che fornisce visitatori interessati
                ArrayList<Visitor> visitors = dao.getToBeNotifiedVisitors(a);
                //emailing ai visitatori
                if (visitors.size() > 0) {
                    String messageToSend = "The artwork " + a.getName() + " of the author " + a.getAuthor() + " is now " + a.getStatus();
                    notifier.sendEmail(visitors, "Change of artwork status", messageToSend);
                }
            } else if (as.getClass() == OnDisplay.class) {
                //opera torna allo stato visibile
                VisitorDAO dao = new VisitorDAO();
                ArrayList<Visitor> nlsubscribers = dao.getNLSubscribers();
                //invio email
                if (nlsubscribers.size() > 0) {
                    String messageToSend = "The artwork " + a.getName() + " of the author " + a.getAuthor() + " is now back on display !";
                    notifier.sendEmail(nlsubscribers, "Change of artwork status", messageToSend);
                }
            }
            a.setStatus(as);
            ArtworkDAO adao = new ArtworkDAO();
            adao.updateStatus(a);
        }
    }

    public void cancelItinerary(Itinerary i) throws SQLException {
        ItineraryDAO dao = new ItineraryDAO();
        if (!dao.inUse(i))
            dao.delete(i);
        else
            throw new RuntimeException("Itinerary in use");
    }


}
package main.business_logic;

import main.DomainModel.*;
import main.orm.ArtworkDAO;
import main.orm.ItineraryDAO;
import main.orm.VisitorDAO;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
    private final String emailAddress = "museoswe@gmail.com";
    private final String emailPassword = "usomdxtqjcwbdiid";

    public void addItinerary(int id, String name) throws SQLException {
        Itinerary i = new Itinerary(id, name, new ArrayList<>());
        ItineraryDAO dao = new ItineraryDAO();
        dao.insert(i);
    }

    public void addArtwork(Itinerary i, Artwork a) throws SQLException, MessagingException {
        //se opera non ancora presente nel db viene memorizzata
        ArtworkDAO adao = new ArtworkDAO();
        if(adao.get(a.getCode()) == null){
            adao.insert(a);
        }
        ItineraryDAO idao = new ItineraryDAO();
        idao.addArtworkToItinerary(i, a);
        i.addArtwork(a);

        //notifica dei newsletter subscribers
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> nlsubscribers = vdao.getNLSubscribers();

        //invio email
        if (nlsubscribers.size()>0){
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");  //autenticazione user
            properties.put("mail.smtp.host", "smtp.gmail.com");  //server smtp gmail
            properties.put("mail.smtp.port", "587"); //numero di porta richiesto da gmail
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailAddress, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.setSubject("Nuova opera");
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String artworkData="Nuova opera "+a.getName() +" dell'autore " + a.getAuthor() + " con stato " + a.getStatus() + " presente nell'itinerario " + i.getName();
            String htmlText = "<img src=\"cid:image\" alt=\"Museo di SWE\" style=\"width: 300px; height: 100px; \">\r\n" + //
                    "<h5 style=\"color: gray; font-family: Arial,sans-serif\">"+artworkData+"</h5>\r\n" + //
                    "<div style=\"margin-top: 5em\">\r\n" + //
                    "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Contatti:</p>\r\n" + //
                    "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Telefono: 1234567890</p>\r\n" + //
                    "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Email: museoswe@virgilio.it</p>\r\n" + //
                    "</div>";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("imgs/logo.png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            //message.setContent("Nuova opera "+a.getName() +" dell'autore " + a.getAuthor() + " con stato " + a.getStatus() + " presente nell'itinerario " + i.getName(), "text/html");

            for(Visitor subscriber : nlsubscribers){
                Address addressTo = new InternetAddress(subscriber.getEmailAddress());
                message.addRecipient(Message.RecipientType.TO, addressTo);
            }
            Transport.send(message);
        }
    }

    public ArrayList<Artwork> viewArtworks() throws SQLException{
        ArtworkDAO adao = new ArtworkDAO();
        return adao.getAll();
    }

    public void modifyStatus(Artwork a, ArtworkStatus as) throws SQLException, MessagingException {
        //se lo stato è lo stesso, nessun cambiamento
        //se lo stato da visibile passa a non visibile, notificati i visitatori interessati dalla modifica
        //se lo stato da non visibile passa a visibile notificati i newsletter subscriber
        if(!Objects.equals(as.getStatus(), a.getStatus())){
            if(a.getArtworkStatusObject().getClass() == OnDisplay.class){
                //opera passa ad uno stato non visibile
                VisitorDAO dao = new VisitorDAO();
                //metodo transitive che fornisce visitatori interessati
                ArrayList<Visitor> visitors = dao.getToBeNotifiedVisitors(a);
                //emailing ai visitatori
                if (visitors.size()>0){
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");  //autenticazione user
                    properties.put("mail.smtp.host", "smtp.gmail.com");  //server smtp gmail
                    properties.put("mail.smtp.port", "587"); //numero di porta richiesto da gmail
                    properties.put("mail.smtp.starttls.enable", "true");

                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(emailAddress, emailPassword);
                        }
                    });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailAddress));
                    message.setSubject(a.getName() + " non più visibile");
                    message.setText("L'opera "+ a.getName() +" dell'autore " + a.getAuthor() + " è " + a.getStatus());

                    for(Visitor visitor : visitors){
                        Address addressTo = new InternetAddress(visitor.getEmailAddress());
                        message.addRecipient(Message.RecipientType.TO, addressTo);
                    }
                    Transport.send(message);
                }
            }
            else if(as.getClass() == OnDisplay.class){
                //opera torna allo stato visibile
                VisitorDAO dao = new VisitorDAO();
                ArrayList<Visitor> nlsubscribers = dao.getNLSubscribers();
                //invio email
                if (nlsubscribers.size()>0) {
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");  //autenticazione user
                    properties.put("mail.smtp.host", "smtp.gmail.com");  //server smtp gmail
                    properties.put("mail.smtp.port", "587"); //numero di porta richiesto da gmail
                    properties.put("mail.smtp.starttls.enable", "true");

                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(emailAddress, emailPassword);
                        }
                    });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailAddress));
                    message.setSubject("Nuova opera");
                    message.setText("L'opera " + a.getName() + " dell'autore " + a.getAuthor() + " è tornata allo stato visibile");

                    for (Visitor subscriber : nlsubscribers) {
                        Address addressTo = new InternetAddress(subscriber.getEmailAddress());
                        message.addRecipient(Message.RecipientType.TO, addressTo);
                    }
                    Transport.send(message);
                }
            }
            a.setStatus(as);
            ArtworkDAO adao = new ArtworkDAO();
            adao.updateStatus(a);
        }
    }

    public void cancelItinerary(Itinerary i) throws SQLException{
        ItineraryDAO dao = new ItineraryDAO();
        dao.delete(i);
    }
}

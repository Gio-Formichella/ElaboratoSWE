package main.business_logic;

import main.DomainModel.Itinerary;
import main.DomainModel.Visit;
import main.DomainModel.Visitor;
import main.orm.VisitDAO;
import main.orm.VisitorDAO;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class BookingOffice {
    
    private final String emailAddress = "museoswe@gmail.com";
    private final String emailPassword = "usomdxtqjcwbdiid";

    public void setVisit(int code, String date, String time, int maxVisitors, float price, ArrayList<Itinerary> itineraries) throws SQLException, ParseException{
        VisitDAO dao = new VisitDAO();
        Visit v = new Visit(code, date, time, maxVisitors, price, itineraries);
        dao.insert(v);
    }

    public void cancelVisit(int code) throws SQLException, MessagingException, ParseException{
        VisitDAO dao = new VisitDAO();
        Visit v = dao.getTransitive(code);//serve per il messaggio di notifica
        dao.delete(code);
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
            String itinerariesMessage = "";
            for(Itinerary i:v.getItineraries()){
                itinerariesMessage += i.getName() + " ";
            }
            message.setFrom(new InternetAddress(emailAddress));
            message.setSubject("Visita cancellata");
            message.setText("La visita del giorno "+v.getDate() +" dalle " + v.getTime() + " per gli itinerari\n" + itinerariesMessage + "\n è stata cancellata");

            for(Visitor subscriber : nlsubscribers){
                Address addressTo = new InternetAddress(subscriber.getEmailAddress());
                message.addRecipient(Message.RecipientType.TO, addressTo);
            }
            Transport.send(message);
        }
    }

    public void modifyVisit(Visit v) throws SQLException, ParseException{
        VisitDAO dao = new VisitDAO();
        dao.update(v);
    }

    public ArrayList<Visit> viewVisits() throws SQLException, ParseException{
        VisitDAO dao = new VisitDAO();
        return dao.getAll();
    }
}
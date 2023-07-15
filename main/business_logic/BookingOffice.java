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

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class BookingOffice {


    public void setVisit(int code, String date, String time, int maxVisitors, float price, String language, ArrayList<Itinerary> itineraries) throws SQLException, ParseException {
        if (maxVisitors < 0 || price < 0) {
            throw new SQLException("maxVisitors and price must be positive");
        }
        VisitDAO dao = new VisitDAO();
        Visit v = new Visit(code, date, time, maxVisitors, price, language, itineraries);
        dao.insert(v);
    }

    public void cancelVisit(int code) throws SQLException, MessagingException, ParseException {
        VisitDAO dao = new VisitDAO();
        Visit v = dao.getTransitive(code);//serve per il messaggio di notifica
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> toBeNotifiedVisitors = vdao.getToBeNotifiedVisitors(v);
        StringBuilder itinerariesMessage = new StringBuilder();
        Notifier notifier = Notifier.getInstance();
        for (Itinerary i : v.getItineraries()) {
            itinerariesMessage.append(i.getName()).append(" ");
        }
        String messageToSend = "The visit of the " + v.getDate() + " starting at " + v.getTime() + " for the itineraries: \n" + itinerariesMessage + " has been cancelled";
        dao.delete(code);
        //invio email
        if (toBeNotifiedVisitors.size() > 0) {
            notifier.sendEmail(toBeNotifiedVisitors, "Visit has been cancelled", messageToSend);
        }
    }

    public void modifyVisit(Visit v) throws SQLException, MessagingException, ParseException {
        if (v.getMaxVisitors()< 0 || v.getPrice() < 0) {
            throw new SQLException("maxVisitors and price must be positive");
        }
        VisitDAO dao = new VisitDAO();
        Visit vOld = dao.getTransitive(v.getCode());
        StringBuilder oldItinerariesMessage = new StringBuilder();
        Notifier notifier = Notifier.getInstance();
        for (Itinerary i : v.getItineraries()) {
            oldItinerariesMessage.append(i.getName()).append(" ");
        }
        String oldVisitMessage = "The visit of the " + vOld.getDate() + " starting at " + vOld.getTime() + " conducted in " + vOld.getLanguage() + " for the itineraries: \n" + oldItinerariesMessage;
        dao.update(v);
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> toBeNotifiedVisitors = vdao.getToBeNotifiedVisitors(v);

        //invio email
        if (toBeNotifiedVisitors.size() > 0) {
            String messageToSend = oldVisitMessage + " has been changed. The visit will be held the " + v.getDate() + " and start at " + v.getTime() + " and conducted in " + v.getLanguage();
            notifier.sendEmail(toBeNotifiedVisitors, "Visit change", messageToSend);
        }
    }

    public ArrayList<Visit> viewVisits() throws SQLException, ParseException {
        VisitDAO dao = new VisitDAO();
        return dao.getAll();
    }


}
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

    private final String emailAddress = "museoswe@gmail.com";
    private final String emailPassword = "usomdxtqjcwbdiid";

    public void setVisit(int code, String date, String time, int maxVisitors, float price, ArrayList<Itinerary> itineraries) throws SQLException, ParseException {
        if (maxVisitors < 0 || price < 0) {
            throw new SQLException("maxVisitors and price must be positive");
        }
        VisitDAO dao = new VisitDAO();
        Visit v = new Visit(code, date, time, maxVisitors, price, itineraries);
        dao.insert(v);
    }

    public void cancelVisit(int code) throws SQLException, MessagingException, ParseException {
        VisitDAO dao = new VisitDAO();
        Visit v = dao.getTransitive(code);//serve per il messaggio di notifica
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> toBeNotifiedVisitors = vdao.getToBeNotifiedVisitors(v);
        StringBuilder itinerariesMessage = new StringBuilder();
        for (Itinerary i : v.getItineraries()) {
            itinerariesMessage.append(i.getName()).append(" ");
        }
        String messageToSend = "The visit of the " + v.getDate() + " starting at " + v.getTime() + " for the itineraries: \n" + itinerariesMessage + " has been cancelled";
        dao.delete(code);
        //invio email
        if (toBeNotifiedVisitors.size() > 0) {
            sendEmail(toBeNotifiedVisitors, "Visit has been canceld", messageToSend);
        }
    }

    public void modifyVisit(Visit v) throws SQLException, MessagingException, ParseException {
        VisitDAO dao = new VisitDAO();
        Visit vOld = dao.getTransitive(v.getCode());
        StringBuilder oldItinerariesMessage = new StringBuilder();
        for (Itinerary i : v.getItineraries()) {
            oldItinerariesMessage.append(i.getName()).append(" ");
        }
        String oldVisitMessage = "The visit of the " + vOld.getDate() + " starting at " + vOld.getTime() + " for the itineraries: \n" + oldItinerariesMessage;
        dao.update(v);
        VisitorDAO vdao = new VisitorDAO();
        ArrayList<Visitor> toBeNotifiedVisitors = vdao.getToBeNotifiedVisitors(v);

        //invio email
        if (toBeNotifiedVisitors.size() > 0) {
            StringBuilder itinerariesMessage = new StringBuilder();
            for (Itinerary i : v.getItineraries()) {
                itinerariesMessage.append(i.getName()).append(" ");
            }
            String messageToSend = oldVisitMessage + " has been changed. The visit will be held the " + v.getDate() + " and start at " + v.getTime() + " and include the following itineraries \n" + itinerariesMessage;
            sendEmail(toBeNotifiedVisitors, "Visit change", messageToSend);
        }
    }

    public ArrayList<Visit> viewVisits() throws SQLException, ParseException {
        VisitDAO dao = new VisitDAO();
        return dao.getAll();
    }

    private void sendEmail(ArrayList<Visitor> toBeNotified, String subject, String messageToSend) throws MessagingException {
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
        message.setSubject(subject);
        for (Visitor v : toBeNotified) {
            Address addressTo = new InternetAddress(v.getEmailAddress());
            message.addRecipient(Message.RecipientType.TO, addressTo);
        }
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");

        //first part of the message
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<img src=\"cid:image\" alt=\"Museo di SWE\" style=\"width: 300px; height: 100px; \">\r\n" + //
                "<h5 style=\"color: gray; font-family: Arial,sans-serif\">" + messageToSend + "</h5>\r\n" + //
                "<div style=\"margin-top: 5em\">\r\n" + //
                "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Contatti:</p>\r\n" + //
                "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Telefono: 1234567890</p>\r\n" + //
                "  <p style=\"color: gray; font-family: Arial,sans-serif; font-size: 0.7em\">Email: museoswe@gmail.com</p>\r\n" + //
                "</div>";
        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);

        //second part of the message
        messageBodyPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource("imgs/logo.png");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");
        messageBodyPart.setFileName("logo.png");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
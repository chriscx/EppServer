/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.epp;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author xuanzhaopeng
 */
public class Mail {

    public void sendMail(String toAdd, String url,String[] group) throws SendFailedException {
        final String username = "eclipseplusplus14@gmail.com";
        final String password = "eclipsepp";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xuanzhaopeng@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAdd));
            message.setSubject("Eclipse++ Build Platform - Donwload your eclipse now");
            StringBuilder html = new StringBuilder();
            html.append("<body><p>Dear Client</p>");
            html.append("<p>You just now build a eclipse with following SPL Features:");
            html.append("<ul>");
            for(String feature :  group){
                html.append("<li>" + feature + "</li>");
            }
            html.append("</ul></p>");
            html.append("<p>You could download it from <a href=\"" + url +"\">Click Here</a></p>");
            
            message.setText(html.toString(),"utf-8", "html");

            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //return flag; 
    }
}

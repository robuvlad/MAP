package sample.service;

import com.sun.mail.smtp.SMTPSenderFailedException;
import sample.domain.Email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

public class EmailService implements Runnable {

    private Email email;
    private String subject, body;

    private String USER_NAME = "proiectmap2019";
    private String PASSWORD = "parolaputernica";

    public EmailService(Email email, String subject, String body) {
        this.email = email;
        this.subject = subject;
        this.body = body;
    }

    public Email getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public void run() {
        sendEmail(email);
    }

    private Email sendEmail(Email email){
        System.out.println("Email : " + email.getReceiver());
        setAndSendEmail();
        return email;
    }

    private void setAndSendEmail(){
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { email.getReceiver() };
        String subject = this.subject;
        String body = this.body;

        sendFromGMail(from, pass, to, subject, body);
    }

    private void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.getStackTrace();
        }
        catch (MessagingException me) {
            me.getStackTrace();
        }
    }
}

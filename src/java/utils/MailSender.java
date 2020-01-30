/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Fran
 */
public class MailSender {

// Server mail user & pass
    private String user = null;
    private String pass = null;

    // DNS Host + SMTP Port
    private String smtp_host = null;
    private int smtp_port = 0;

    // Default DNS Host + port
    private static final String DEFAULT_SMTP_HOST = "smtp.gmail.com";
    private static final int DEFAULT_SMTP_PORT = 25;

    /**
     * Disabled
     */
    @SuppressWarnings("unused")
    private MailSender() {
    }

    /**
     * Builds the EmailService. If the Server DNS and/or Port are not provided,
     * default values will be loaded
     *
     * @param user User account login
     * @param pass User account password
     * @param host The Server DNS
     * @param port The Port
     */
    public MailSender(String user, String pass, String host, String port) {
        this.user = user;
        this.pass = pass;
        this.smtp_host = (host == null ? DEFAULT_SMTP_HOST : host);
        this.smtp_port = (port == null ? DEFAULT_SMTP_PORT : new Integer(port).intValue());
    }

    /**
     * Sends the given <b>text</b> from the <b>sender</b> to the
     * <b>receiver</b>. In any case, both the <b>sender</b> and <b>receiver</b>
     * must exist and be valid mail addresses. <br/>
     * <br/>
     *
     * Note the <b>user</b> and <b>pass</b> for the authentication is provided
     * in the class constructor. Ideally, the <b>sender</b> and the <b>user</b>
     * coincide.
     *
     * @param sender The mail's FROM part
     * @param receiver The mail's TO part
     * @param subject The mail's SUBJECT
     * @param text The proper MESSAGE
     * @throws MessagingException Is something awry happens
     *
     */
    public void sendMail(String sender, String receiver, String subject, String text) throws MessagingException {

        // Mail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtp_host);
        properties.put("mail.smtp.port", smtp_port);
        properties.put("mail.smtp.ssl.trust", smtp_host);
        properties.put("mail.imap.partialfetch", false);

        // Authenticator knows how to obtain authentication for a network connection.
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        // MIME message to be sent
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender)); // Ej: emisor@gmail.com
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver)); // Ej: receptor@gmail.com
        message.setSubject(subject); // Asunto del mensaje

        // A mail can have several parts
        Multipart multipart = new MimeMultipart();

        // A message part (the message, but can be also a File, etc...)
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text, "text/html");
        multipart.addBodyPart(mimeBodyPart);

        // Adding up the parts to the MIME message
        message.setContent(multipart);

        // And here it goes...
        Transport.send(message);
    }

    /**
     * True if the mail is not null and a valid email address, false otherwise.
     *
     * @param mail The email address
     * @return True or False
     */
    public static boolean isValid(String mail) {
        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");
        if (mail == null) {
            return false;
        }
        return pattern.matcher(mail).matches();
    }
}

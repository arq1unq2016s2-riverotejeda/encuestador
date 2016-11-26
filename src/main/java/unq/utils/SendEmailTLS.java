package unq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by mrivero on 26/11/16.
 */
public class SendEmailTLS {

    private static Logger LOGGER = LoggerFactory.getLogger(SendEmailTLS.class);

    public static void sendEmailSurveyNotification(String studentName, String studentEmail,
                                                   String surveyUrl){
        final String username = "survey.unq@gmail.com";
        final String password = "SurveyUNQ2016";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        LOGGER.info(String.format("Starting auth for sending mail notification to user %s", username));

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(studentEmail));
            message.setSubject("Completa la encuesta UNQ");
            message.setText(String.format("Hola %s,"
                    + "\n\n Completa la encuesta de pre inscripcion con esta url: %s",studentName, surveyUrl));

            Transport.send(message);

        } catch (MessagingException e) {
            LOGGER.error(String.format("Error trying to send email to %s", studentEmail), e);
            throw new RuntimeException(e);
        }

        LOGGER.info(String.format("Finish sending mail notification to user %s", username));
    }
}

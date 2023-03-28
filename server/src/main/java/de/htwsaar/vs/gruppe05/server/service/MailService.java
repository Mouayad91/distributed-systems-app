package de.htwsaar.vs.gruppe05.server.service;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * Send email using Spring Email
     * Please add SMTP information in application.properties in spring.mail section
     * @param subject Email subject
     * @param text Email text or content
     * @param to Recipient Email
     */
    void sendEmail(String subject, String text, String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text,"text/html; charset=utf-8");
        message.setFrom(from);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.addRecipients(Message.RecipientType.TO,to);
        message.setContent(multipart);
        message.setSubject(subject);

        mailSender.send(message);
    }
}
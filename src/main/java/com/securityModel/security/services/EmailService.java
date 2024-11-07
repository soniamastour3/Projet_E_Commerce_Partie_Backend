package com.securityModel.security.services;
import com.securityModel.modele.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerSimpleMessage(final Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        message.setTo(mail.getTo());
        message.setFrom(mail.getFrom());
        mailSender.send(message);
    }

    public void sendSimpleMessage(String email, String temporaryPassword, String s) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(s);
        message.setText("Hello,\n\nYour temporary password is: " + temporaryPassword +
                "\nPlease use this password to log in and remember to change it as soon as possible." +
                "\n\nThank you.");

        // Send the email
        mailSender.send(message);
    }
}
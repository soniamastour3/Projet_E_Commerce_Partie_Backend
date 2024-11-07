//package com.securityModel.security.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        //Définit le serveur SMTP de Gmail comme hôte pour l'envoi d'e-mails.
//        mailSender.setHost("smtp.gmail.com");
//        //Définit le port utilisé pour la communication avec le serveur SMTP. Le port 587 est généralement utilisé pour la connexion sécurisée via STARTTLS.
//        mailSender.setPort(587);
//        mailSender.setUsername("soniamastour.sm@gmail.com");
//        mailSender.setPassword("xfwx eufc wmpi buwf");
//
//        Properties props = mailSender.getJavaMailProperties();
//        //Spécifie que le protocole de transport utilisé est SMTP.
//        props.put("mail.transport.protocol", "smtp");
//        //Active l'authentification SMTP, ce qui est requis pour se connecter au serveur SMTP.
//        props.put("mail.smtp.auth", "true");
//        // Active STARTTLS, qui permet une connexion sécurisée.
//        props.put("mail.smtp.starttls.enable", "true");
//        // Active le mode debug pour le débogage des communications SMTP.
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//}

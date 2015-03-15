package uk.co.artymonkeys.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

public class EmailSender extends HttpServlet {

    final String username = "artymonkeys@gmail.com";
    final String password = "oYIn9GTMJ4Ba";

    final Properties props = new Properties();

    public EmailSender() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String to = "andyjcboyle@gmail.com";
        String from = req.getParameter("email");
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String body = req.getParameter("message");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(IsGeneralEnquiry(body) ? "Enquiry To Arty Monkeys" : "Registration");
            message.setText("Name:\t\t" + name + "\nPhone:\t\t" + phone + "\nEmail:\t\t" + from
                    + (IsGeneralEnquiry(body) ? "\n\n" + body : ""));

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

        resp.sendRedirect("index.html");
    }

    private boolean IsGeneralEnquiry(String body) {
        return body != null;
    }

}

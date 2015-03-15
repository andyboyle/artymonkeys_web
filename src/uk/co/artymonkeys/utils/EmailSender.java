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

            MimeMessage messageResp = new MimeMessage(session);
            messageResp.setFrom(new InternetAddress("no-reply@artymonkeys.co.uk"));
            messageResp.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
            messageResp.setSubject(IsGeneralEnquiry(body) ? SUBJECT_ENQUIRY : SUBJECT_REGISTRATION);
            messageResp.setText("Hi " + name + "\n\n" + (IsGeneralEnquiry(body) ? BODY_ENQUIRY : BODY_REGISTRATION));
            Transport.send(messageResp);

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        PrintWriter out = resp.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('Email Sent Successfully');");
        out.println("location='index.html';");
        out.println("</script>");
    }

    private boolean IsGeneralEnquiry(String body) {
        return body != null;
    }

    final private static String SUBJECT_REGISTRATION = "Confirmation Of Interest In Arty Monkeys";
    final private static String SUBJECT_ENQUIRY = "Confirmation Of Enquiry To Arty Monkeys";
    final private static String BODY_REGISTRATION = "Thank you for registering interest in Arty Monkeys." +
            " We will keep you informed of any upcoming classes and events in future.\n\n\n" +
            "Kind Regards,\n\nYvonne & Kelley";
    final private static String BODY_ENQUIRY = "Thank you for your enquiry to Arty Monkeys.\n\n\n" +
            "We look forward to responding to you promptly.\n\n\n" +
            "Kind Regards,\n\nYvonne & Kelley";
}

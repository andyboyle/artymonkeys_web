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

    final String username = "no-reply@artymonkeys.co.uk";
    final String password = "x6AyIMTve4Wr";

    final Properties props = new Properties();

    public EmailSender() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.fasthosts.co.uk");
        props.put("mail.smtp.port", "587");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String to = "info@artymonkeys.co.uk";
//        String to = "andyjcboyle@gmail.com";
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

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(IsGeneralEnquiry(body) ? "Enquiry To Arty Monkeys" : "Registration");
            message.setText("Name:\t\t" + (name != null ? name : "") +
                    "\nPhone:\t\t" + (phone != null ? phone : "") +
                    "\nEmail:\t\t" + (from != null ? from : "") +
                    (IsGeneralEnquiry(body) ? "\n\n" + body : ""));
            Transport.send(message);

            MimeMessage messageResp = new MimeMessage(session);
            messageResp.setFrom(new InternetAddress(username));
            messageResp.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
            messageResp.setSubject(IsGeneralEnquiry(body) ? SUBJECT_ENQUIRY : SUBJECT_REGISTRATION);
            messageResp.setText("Hi " + (name != null ? name : "") + "\n\n" +
                    (IsGeneralEnquiry(body) ? BODY_ENQUIRY : BODY_REGISTRATION) +
                    AUTOMATED_MSG);
            Transport.send(messageResp);
            out.println("<script>");
            out.println("alert('Email Sent Successfully');");
            out.println("location='index.html';");
            out.println("</script>");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            out.println("<script>");
            out.println("alert('Error Sending.');");
            out.println("location='index.html';");
            out.println("</script>");
        } finally {
            out.flush();
            out.close();
        }
    }

    private boolean IsGeneralEnquiry(String body) {
        return body != null;
    }

    final private static String SUBJECT_REGISTRATION = "Confirmation Of Interest In Arty Monkeys";
    final private static String SUBJECT_ENQUIRY = "Confirmation Of Enquiry To Arty Monkeys";
    final private static String BODY_REGISTRATION = "Thanks for your interest in Arty Monkeys, " +
            "we'll be in touch soon.\n\n\n" +
            "Kind Regards,\n\nYvonne & Kelley\n";
    final private static String BODY_ENQUIRY = "Thanks your enquiry to Arty Monkeys.\n\n\n" +
            "We'll respond to your query as soon as possible.\n\n\n" +
            "Kind Regards,\n\nYvonne & Kelley\n";
    final private static String AUTOMATED_MSG = "\n\n*** This is an automated message, please don't reply directly. Thanks! ***";
}

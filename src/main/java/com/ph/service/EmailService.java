package com.ph.service;

import com.ph.domain.entities.TourRequest;
import com.ph.exception.customs.EmailSendingException;
import com.ph.utils.MessageUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final MessageUtil messageUtil;

    @Value("${email.account.forgot.template}")
    private String emailTemplate;

    @Value("${email.account.forgot.subject}")
    private String emailSubject;

    @Value("${email.account.forgot.email-img}")
    private String mailImage;

    @Value("${email.account.confirmation.template}")
    private String confirmEmailTemplate;

    @Value("${email.account.confirmation.subject}")
    private String confirmEmailSubject;

    @Value("${email.account.check-tour-request.template}")
    private String checkTourRequestTemplate;

    @Value("${email.account.check-tour-request.subject}")
    private String informationEmailSubject;


    /**
     * Sends a reset password email to the specified recipient email address with the given reset code.
     *
     * @param recipientEmail The email address of the recipient.
     * @param resetCode      The reset code to be included in the email.
     * @throws EmailSendingException If there is an error sending the email.
     */
    public void sendResetPasswordEmail(String recipientEmail, String resetCode) {
        try {
            // Create a new MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Create a new MimeMessageHelper and set the necessary properties
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            // Get the email template
            Template template = freemarkerConfig.getTemplate(emailTemplate);

            // Create a StringWriter to store the processed template
            StringWriter writer = new StringWriter();

            // Set the data model for the template
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("code", resetCode);
            dataModel.put("imgSource", mailImage);

            // Process the template with the data model and store the result in the StringWriter
            template.process(dataModel, writer);
            String emailContent = writer.toString();

            // Set the recipient email address and subject of the email
            helper.setTo(recipientEmail);
            helper.setSubject(emailSubject);

            // Set the email content as HTML and send the email
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            // If there is an error sending the email, throw an EmailSendingException with the appropriate message
            throw new EmailSendingException(String.format(messageUtil.getMessage("error.mail.reset-password"), e.getMessage()));
        }
    }

    @Async
    public void sendEmail(MimeMessage email) {
        mailSender.send(email);
    }

    public void sendConfirmationMail(String userMail, String token, LocalDateTime createdTime) {
        try {
            // Create a new MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Create a new MimeMessageHelper and set the necessary properties
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            // Get the email template
            Template template = freemarkerConfig.getTemplate(confirmEmailTemplate);

            // Create a StringWriter to store the processed template
            StringWriter writer = new StringWriter();

            // Set the data model for the template
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("token", token);
            dataModel.put("imgSource", mailImage);


            // Calculate 24 hours from now
            LocalDateTime expirationTime = createdTime.plusHours(24);

            dataModel.put("expirationTime", expirationTime);

            // Process the template with the data model and store the result in the StringWriter
            template.process(dataModel, writer);
            String emailContent = writer.toString();

            // Set the recipient email address and subject of the email
            helper.setTo(userMail);
            helper.setSubject(confirmEmailSubject);

            // Set the email content as HTML and send the email
            helper.setText(emailContent, true);
            sendEmail(message);
        } catch (MessagingException | IOException | TemplateException e) {
            // If there is an error sending the email, throw an EmailSendingException with the appropriate message
            throw new EmailSendingException(String.format(messageUtil.getMessage("error.mail.confirm-account"), e.getMessage()));
        }
    }



    public void sendInformationEmail(TourRequest tourRequest) {
        try {
            // Create a new MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Create a new MimeMessageHelper and set the necessary properties
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            // Get the email template
            Template template = freemarkerConfig.getTemplate(checkTourRequestTemplate);

            // Create a StringWriter to store the processed template
            StringWriter writer = new StringWriter();

            // Set the data model for the template
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("imgSource", mailImage);
            dataModel.put("firstName", tourRequest.getGuestUser().getFirstName());
            dataModel.put("lastName", tourRequest.getGuestUser().getLastName());
            dataModel.put("tourTime", tourRequest.getTourTime());
            dataModel.put("tourDate", tourRequest.getTourDate());
            dataModel.put("advertTitle", tourRequest.getAdvert().getTitle());
            dataModel.put("country", tourRequest.getAdvert().getCountry().getName());
            dataModel.put("city", tourRequest.getAdvert().getCity().getName());
            dataModel.put("district", tourRequest.getAdvert().getDistrict().getName());


            // Process the template with the data model and store the result in the StringWriter
            template.process(dataModel, writer);
            String emailContent = writer.toString();

            // Set the recipient email address and subject of the email
            helper.setTo(tourRequest.getGuestUser().getEmail());
            helper.setSubject(informationEmailSubject);

            // Set the email content as HTML and send the email
            helper.setText(emailContent, true);
            sendEmail(message);
        } catch (MessagingException | IOException | TemplateException e) {
            // If there is an error sending the email, throw an EmailSendingException with the appropriate message
            throw new EmailSendingException(String.format(messageUtil.getMessage("error.mail.check-tour-request"), e.getMessage()));
        }
    }

}

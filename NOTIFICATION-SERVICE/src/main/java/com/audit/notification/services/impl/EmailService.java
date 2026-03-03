package com.audit.notification.services.impl;

import com.audit.notification.RabbitMQ.template.EmailTemplate;
import com.audit.notification.RabbitMQ.template.OtpTemplate;
import com.audit.notification.model.EmailNotificationLog;
import com.audit.notification.model.NotificationStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailLogServiceImpl emailLogService;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, EmailLogServiceImpl emailLogService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailLogService = emailLogService;
    }

    public void sendWelcomeEmail(EmailTemplate emailTemplate) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(fromEmail);
//        message.setTo(toEmail);
//        message.setSubject("Welcome to Our Platform");
//        message.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\nSmart Audit Team");
//        mailSender.send(message);

        try{
            Context context = new Context();
            context.setVariable("name", emailTemplate.getName());
            context.setVariable("email", emailTemplate.getRecipientEmail());
            context.setVariable("body", emailTemplate.getBody());
            context.setVariable("subject", emailTemplate.getSubject());
            context.setVariable("senderEmail", emailTemplate.getSenderEmail());

            String process = templateEngine.process("welcome_email_template", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom(fromEmail);
            helper.setTo(emailTemplate.getRecipientEmail());
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(process, true);

            mailSender.send(mimeMessage);
        }
        catch (MessagingException me){

            EmailNotificationLog emailLog = EmailNotificationLog.builder()
                    .recipientEmail(emailTemplate.getRecipientEmail())
                    .subject(emailTemplate.getSubject())
                    .body(emailTemplate.getBody())
                    .status(NotificationStatus.FAILED)
                    .build();
            emailLogService.saveEmailNotificationLog(emailLog);
        }
        finally {

            EmailNotificationLog emailLog = EmailNotificationLog.builder()
                    .recipientEmail(emailTemplate.getRecipientEmail())
                    .subject(emailTemplate.getSubject())
                    .body(emailTemplate.getBody())
                    .status(NotificationStatus.SENT)
                    .build();
            emailLogService.saveEmailNotificationLog(emailLog);

        }



    }


    public void sendOtpEmail(OtpTemplate template, String htmlTemplate) {

        try{
            Context context = new Context();
            context.setVariable("recipientEmail", template.getRecipientEmail());
            context.setVariable("senderEmail", template.getSenderEmail());
            context.setVariable("otp", template.getOtp());

            String process = templateEngine.process(htmlTemplate, context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom(fromEmail);
            helper.setTo(template.getRecipientEmail());
            helper.setSubject(template.getSubject());
            helper.setText(process, true);

            mailSender.send(mimeMessage);
        }
        catch (MessagingException me){

            EmailNotificationLog emailLog = EmailNotificationLog.builder()
                    .recipientEmail(template.getRecipientEmail())
                    .subject(template.getSubject())
                    .body(me.getMessage())
                    .status(NotificationStatus.FAILED)
                    .build();
            emailLogService.saveEmailNotificationLog(emailLog);

        }

        finally {

            EmailNotificationLog emailLog = EmailNotificationLog.builder()
                    .recipientEmail(template.getRecipientEmail())
                    .subject(template.getSubject())
                    .body("")
                    .status(NotificationStatus.SENT)
                    .build();
            emailLogService.saveEmailNotificationLog(emailLog);

        }

    }

    public void sendResetOtpEmail(String toEmail, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("email", toEmail);
        context.setVariable("otp", otp);

        String process = templateEngine.process("password-reset-email", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Forgot your password?");
        helper.setText(process, true);

        mailSender.send(mimeMessage);
    }
}

package ua.com.gup.listener.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.com.gup.mongo.composition.domain.email.EmailMessage;
import ua.com.gup.service.EmailService;

@Component
@Profile("pre-prod")
public class SendMailTask {

    private static Logger LOG = LoggerFactory.getLogger(SendMailTask.class);

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 * * * * *")
    public void printMessages() {
        EmailMessage message = emailService.findOneMessageInQueue();
        if (message != null) {
            try {
                emailService.sendEmail(message);                
            } catch (Exception e) {
                LOG.info("Sending mail exception", e);
                emailService.updateLastAttemptTimestamp(message);
            }
        }
    }
}

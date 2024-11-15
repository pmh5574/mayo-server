package com.mayo.server.chef.infra;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleEmailService {

    private final AmazonSimpleEmailService sesClient;

    public static final String FROM = "mayoseoul@gmail.com";

    public void send(
            String subject,
            String htmlBody,
            String to
    ) {

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(htmlBody)
                                )
                        )
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(FROM);
        sesClient.sendEmail(request);

    }

}

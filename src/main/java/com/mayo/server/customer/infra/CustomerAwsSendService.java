package com.mayo.server.customer.infra;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.mayo.server.common.enums.EmailTemplate;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.AwsSystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@RequiredArgsConstructor
@Service
public class CustomerAwsSendService {

    private final SnsClient snsClient;
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void snsSend(String message, String phone) {
        try {
            snsClient.publish(getRequest(message, phone));
        } catch (Exception e) {
            if(e instanceof SnsException) {
                throw new AwsSystemException(ErrorCode.AWS_SNS_EXCEPTION);
            }
            throw new RuntimeException(e);
        }
    }

    private PublishRequest getRequest(String message, String phone) {
        return PublishRequest
                .builder()
                .message(message)
                .phoneNumber(phone)
                .build();
    }


    public void emailSend(String subject, String htmlBody, String to) {
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
                .withSource(EmailTemplate.FROM.getText());
        amazonSimpleEmailService.sendEmail(request);

    }

}

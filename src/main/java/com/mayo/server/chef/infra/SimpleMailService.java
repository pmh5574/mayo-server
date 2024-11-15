package com.mayo.server.chef.infra;

import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.AwsSystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Component
@RequiredArgsConstructor
public class SimpleMailService {

    private final SnsClient snsClient;

     public void send(
             String message,
             String phone
     ) {

         try {

             snsClient.publish(getRequest(
                     message,
                     phone
             ));

         } catch (Exception e) {

             if(e instanceof SnsException) {
                 throw new AwsSystemException(ErrorCode.AWS_SNS_EXCEPTION);
             }

             throw new RuntimeException(e);
         }

     }

    public PublishRequest getRequest(
            String message,
            String phone
    ) {

        return PublishRequest
                .builder()
                .message(message)
                .phoneNumber(phone)
                .build();
    }

}

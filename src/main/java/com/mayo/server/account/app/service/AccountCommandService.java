package com.mayo.server.account.app.service;

import com.mayo.server.account.adapter.in.web.AccountRequest;
import com.mayo.server.account.app.port.in.AccountCommandUseCase;
import com.mayo.server.account.app.port.in.AccountInputPort;
import com.mayo.server.account.domain.models.BankCode;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountCommandService implements AccountCommandUseCase {

    private final RestTemplate restApi;
    private final AccountInputPort accountInputPort;

    @Override
    public void bank(Long id, AccountRequest.BankRequest req) {

        if(Objects.isNull(BankCode.getCode(req.bankCode()))) {
            throw new InvalidRequestException(
                    ErrorCode.CHEF_BANK_NOT_FOUND,
                    ErrorCode.CHEF_BANK_NOT_FOUND.getMessage()
            );
        }

        accountInputPort.saveBank(id, req.bankCode());
    }

    @Override
    public void account(Long id, AccountRequest.AccountRegisterRequest req) {

        accountInputPort.saveAccount(id, req.account());
    }


//    @Override
//    @Transactional
//    public void bank(AccountRequest.AccountRegisterRequest req) {
//
//        URI uri = URI.create("https://openapi.openbanking.or.kr/v2.0/inquiry/real_name");
//
//        System.out.println(getAccessToken1(req.bankCode(), req.account()));

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setBearerAuth("Bearer 8fcc9a6a-c13f-49cd-97ed-192cea5d57e4");
//        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
//        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        JSONObject json = new JSONObject();
//        String uuid = UUID.randomUUID().toString();
//
//        json.put("bank_tran_id", uuid);
//        json.put("bank_code_std", req.bankCode());
//        json.put("account_num", "65270204482577");
//        json.put("accoun_seq", "001");
//        json.put("account_holder_info_type", "1");
//        json.put("account_holder_info", "950121");
//        json.put("tran_dtime", "");
//
//        String accessToken = restApi.getForObject(uri, String.class);
//    }

//    public String getAccessToken1(String bank_code, String bank_num) {
//
//        HashMap map = new HashMap<>();
//        String impKey = "abef85df-44a1-4080-a8c3-e98786a122c6";
//        String impSecret = "8fcc9a6a-c13f-49cd-97ed-192cea5d57e4";
//
//        URI uri = URI.create("https://api.iamport.kr/users/getToken");
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
////        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
////        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        JSONObject json = new JSONObject();
//        String uuid = UUID.randomUUID().toString();
//
//        json.put("imp_key", impKey);
//        json.put("imp_secret", impSecret);
//
//        HttpEntity<JSONObject> entity = new HttpEntity<>(json, httpHeaders);
//
//        ResponseEntity<String> response = restApi.postForEntity(uri, entity, String.class);
//
//        System.out.println("Response: " + response);
//
//        return response.getBody();
//
//
//    }

    @Override
    public void birthDay() {

    }
}

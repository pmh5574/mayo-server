package com.mayo.server.payment.app.service;

import com.fasterxml.jackson.core.JsonParser;
import com.mayo.server.common.Constants;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.party.app.port.in.PartyBoardInputPort;
import com.mayo.server.payment.adapter.in.web.PayCheckSumRequest;
import com.mayo.server.payment.adapter.in.web.PayDraftRequest;
import com.mayo.server.payment.adapter.in.web.PayMetadata;
import com.mayo.server.payment.adapter.in.web.PayRequest;
import com.mayo.server.payment.app.port.in.*;
import com.mayo.server.payment.domain.models.Event;
import com.mayo.server.payment.domain.models.Pay;
import com.mayo.server.payment.domain.models.PayTypeCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.mayo.server.common.Constants.PAY_AGGREGATE_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayCommandService implements PayCommandUseCase {

    private final PayCommand payCommand;
    private final PayQuery payQuery;

    @Override
    @Transactional
    public void handle(PayDraftRequest req) {

        payCommand.save(Event
                .builder()
                        .eventType(PayTypeCode.DRAFT.getCode())
                        .aggregateType(PAY_AGGREGATE_TYPE)
                        .version(1L)
                        .aggregateId(req.orderId())
                        .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                        .data(Event.parsedStringEvent(
                                new PayDraftMetadata(
                                        req.metadata().customerKey(),
                                        req.metadata().customerId(),
                                        req.metadata().customerHomePartyIds(),
                                        req.orderName(),
                                        req.amount(),
                                        req.customerName()
                                )
                        ))
                .build());
    }

    @Override
    @Transactional
    public void handle(PayCheckSumRequest req) {

        JSONParser jsonParser = new JSONParser();

        Event event = payQuery.getLatestEvent(req.orderId());
        if(Objects.isNull(event)) {
            throw new NotFoundException(ErrorCode.PAYMENT_DRAFT_NOT_FOUND);
        }

        PayDraftMetadata metadata = event.getMetadata(jsonParser, PayDraftMetadata.class);
        if(!Objects.equals(metadata.amount(), req.amount())) {
            log.warn("Check Sum Event Not Equal Amount Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        payCommand.save(Event
                .builder()
                .eventType(PayTypeCode.CHECK_SUM.getCode())
                .aggregateType(PAY_AGGREGATE_TYPE)
                .version(2L)
                .aggregateId(event.getAggregateId())
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .data(Event.parsedStringEvent(req))
                .build());

    }

    @Override
    @Transactional
    public void handle(PayRequest req) {

        JSONParser jsonParser = new JSONParser();

        Event checkSumEvent = payQuery.getLatestEvent(req.orderId());
        if(Objects.isNull(checkSumEvent)) {
            log.warn("Check Sum Event Not found Aggregate Id :" + req.orderId());
            throw new NotFoundException(ErrorCode.PAYMENT_CHECKSUM_NOT_FOUND);
        }

        PayCheckSumMetadata checkSumMetadata = checkSumEvent.getMetadata(jsonParser, PayCheckSumMetadata.class);
        if(!Objects.equals(checkSumMetadata.amount(), req.totalAmount())) {
            log.warn("Check Sum Event Not Equal Amount Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        if(!Objects.equals(checkSumMetadata.paymentKey(), req.paymentKey())) {
            log.warn("Check Sum Event Not Equal Payment Key Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        Event draftEvent = payQuery.getOldestEvent(req.orderId());
        if(Objects.isNull(draftEvent)) {
            log.warn("Draft Event Not Found Aggregate Id :" + req.orderId());
            throw new NotFoundException(ErrorCode.PAYMENT_DRAFT_NOT_FOUND);
        }

        PayDraftMetadata draftMetadata = draftEvent.getMetadata(jsonParser, PayDraftMetadata.class);
        if(!Objects.equals(draftMetadata.amount(), req.totalAmount())) {
            log.warn("Draft Event Not Equal Amount Key Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        if(!Objects.equals(draftMetadata.customerId(), req.metadata().customerId())) {
            log.warn("Draft Event Not Equal Customer Id Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        if(!Objects.equals(draftMetadata.customerKey(), req.metadata().customerKey())) {
            log.warn("Draft Event Not Equal Customer Key Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        if(!Objects.equals(draftMetadata.customerHomePartyIds(), req.metadata().customerHomePartyIds())) {
            log.warn("Draft Event Not Equal Customer HomePartyIds Aggregate Id :" + req.orderId());
            throw new InvalidRequestException(ErrorCode.PAYMENT_CHECKSUM_ERROR);
        }

        payCommand.save(Event
                .builder()
                .eventType(PayTypeCode.APPROVE.getCode())
                .aggregateType(PAY_AGGREGATE_TYPE)
                .version(3L)
                .aggregateId(checkSumEvent.getAggregateId())
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .data(Event.parsedStringEvent(req))
                .build());

        payCommand.save(Pay
                .builder()
                        .payAmount(req.totalAmount())
                        .payApprovedAt(req.approvedAt())
                        .payKey(req.paymentKey())
                        .payMethod(req.method())
                        .payMid(req.mid())
                        .payMethodId(checkSumMetadata.methodId())
                        .payRequestedAt(req.requestedAt())
                        .payStatus(req.status())
                        .payVersion(String.valueOf(req.version()))
                        .orderId(req.orderId())
                        .orderName(draftMetadata.orderName())
                        .customerId(draftMetadata.customerId())
                        .customerKey(draftMetadata.customerKey())
                        .customerName(draftMetadata.customerName())
                .build());

        payCommand.save(req.orderId(), req.metadata().customerHomePartyIds());

    }
}

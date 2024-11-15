package com.mayo.server.payment.app.port.in;

import com.mayo.server.payment.adapter.in.web.PayCheckSumRequest;
import com.mayo.server.payment.adapter.in.web.PayDraftRequest;
import com.mayo.server.payment.adapter.in.web.PayRequest;

public interface PayCommandUseCase {

    void handle(PayDraftRequest req);

    void handle(PayCheckSumRequest req);

    void handle(PayRequest req);
}
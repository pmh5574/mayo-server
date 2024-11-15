package com.mayo.server.payment.app.port.in;

import com.mayo.server.payment.domain.models.Event;
import com.mayo.server.payment.domain.models.Pay;

import java.util.List;

public interface PayCommand {

    void save(Event event);

    void save(Pay pay);

    void save(String orderId, List<Long> customerHomePartyIds);
}

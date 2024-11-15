package com.mayo.server.payment.app.port.in;

import com.mayo.server.payment.domain.models.Event;

public interface PayQuery {

    Event getLatestEvent(String aggregateId);

    Event getOldestEvent(String aggregateId);
}

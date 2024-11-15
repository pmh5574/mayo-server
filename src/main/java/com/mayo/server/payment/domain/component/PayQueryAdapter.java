package com.mayo.server.payment.domain.component;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.payment.app.port.in.PayQuery;
import com.mayo.server.payment.domain.models.Event;
import com.mayo.server.payment.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PayQueryAdapter implements PayQuery {

    private final EventRepository eventRepository;

    @Override
    public Event getLatestEvent(String aggregateId) {
        return eventRepository.findTopByAggregateIdOrderByCreatedAtDesc(aggregateId);
    }

    @Override
    public Event getOldestEvent(String aggregateId) {
        return eventRepository.findByAggregateIdOrderByCreatedAtAsc(aggregateId);
    }

}

package com.mayo.server.payment.domain.repository;

import com.mayo.server.payment.domain.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
    select e from Event e WHERE e.aggregateId = :aggregateId ORDER BY e.createdAt DESC LIMIT 1
    """)
    Event findTopByAggregateIdOrderByCreatedAtDesc(@Param("aggregateId") String aggregateId);

    @Query("""
    select e from Event e WHERE e.aggregateId = :aggregateId ORDER BY e.createdAt ASC LIMIT 1
    """)
    Event findByAggregateIdOrderByCreatedAtAsc(String aggregateId);
}

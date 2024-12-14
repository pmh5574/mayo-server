package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.ChefPartyWaitingDto;
import com.mayo.server.party.domain.model.PartySchedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyJpaScheduleRepository extends JpaRepository<PartySchedule, Long> {

    @Query("""
                SELECT s FROM PartySchedule s WHERE s.customerHomeParty.customerHomePartyId =:customerHomePartyId
            """)
    List<PartySchedule> findAllByCustomerHomePartyCustomerHomePartyId(@Param("customerHomePartyId") Long customerHomePartyId);

    @EntityGraph(attributePaths = {"customerHomeParty"})
    @Query("""
            SELECT new com.mayo.server.party.app.port.out.ChefPartyWaitingDto(
                chp.customerHomePartyId,
                chp.partyInfo,
                chp.partySchedule
            )
            FROM PartySchedule ps
            LEFT JOIN CustomerHomeParty AS chp ON ps.customerHomeParty.customerHomePartyId = chp.customerHomePartyId
            WHERE ps.chef.id = :id
                AND ps.isMatched = 0
                AND chp.partyStatus = 'CHEF_NOT_SELECTED'
                AND ps.createdAt BETWEEN :startAt AND :endAt
            ORDER BY chp.createdAt DESC
            """)
    List<ChefPartyWaitingDto> findPendingPartiesByChefId(
            @Param("id") Long id,
            @Param("startAt") String startAt,
            @Param("endAt") String endAt,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"customerHomeParty", "customer"})
    @Query("""
            SELECT COUNT(ps)
            FROM PartySchedule ps
            LEFT JOIN CustomerHomeParty AS chp ON ps.customerHomeParty.customerHomePartyId = chp.customerHomePartyId
            WHERE ps.chef.id = :id
                AND ps.isMatched = 0
                AND chp.partyStatus = 'CHEF_NOT_SELECTED'
                AND ps.createdAt BETWEEN :startAt AND :endAt
            """)
    Long countPendingPartiesByChefId(
            @Param("id") Long id,
            @Param("startAt") String startAt,
            @Param("endAt") String endAt
    );

    @EntityGraph(attributePaths = {"customerHomeParty", "customer"})
    @Query("""
            SELECT new com.mayo.server.party.app.port.out.ChefPartyWaitingDto(
                chp.customerHomePartyId,
                chp.partyInfo,
                chp.partySchedule
            )
            FROM PartySchedule ps
            LEFT JOIN CustomerHomeParty AS chp ON ps.customerHomeParty.customerHomePartyId = chp.customerHomePartyId
            LEFT JOIN Customer AS c ON ps.customer.id = c.id
            WHERE ps.chef.id = :id
                AND ps.isMatched = 1
                AND chp.partyStatus = 'COMPLETED'
                AND chp.partySchedule > :endAt
            """)
    List<ChefPartyWaitingDto> findCompletedPartiesByChefId(
            @Param("id") Long id,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"customerHomeParty", "customer"})
    @Query("""
            SELECT new com.mayo.server.party.app.port.out.ChefPartyWaitingDto(
                chp.customerHomePartyId,
                chp.partyInfo,
                chp.partySchedule
            )
            FROM PartySchedule ps
            LEFT JOIN CustomerHomeParty AS chp ON ps.customerHomeParty.customerHomePartyId = chp.customerHomePartyId
            WHERE ps.chef.id = :id
                AND ps.isMatched = 1
                AND chp.partySchedule < :createdAt
            ORDER BY chp.createdAt DESC
            """)
    List<ChefPartyWaitingDto> findPartiesByChefIdAndScheduleBefore(
            @Param("id") Long id,
            @Param("createdAt") LocalDateTime createdAt,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"customerHomeParty", "customer"})
    @Query("""
            SELECT COUNT(ps)
            FROM PartySchedule ps
            LEFT JOIN CustomerHomeParty AS chp ON ps.customerHomeParty.customerHomePartyId = chp.customerHomePartyId
            WHERE ps.chef.id = :id
                AND ps.isMatched = 1
                AND chp.partySchedule < :createdAt
                AND ps.createdAt BETWEEN :startAt AND :endAt
            """)
    Long countPartiesByChefIdAndScheduleBefore(
            @Param("id") Long id,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("startAt") String startAt,
            @Param("endAt") String endAt
    );

    @Modifying
    @Query(
            "UPDATE PartySchedule AS ps SET ps.isMatched = :isMatched WHERE ps.customerHomeParty.customerHomePartyId =:customerHomePartyId"
    )
    void updateIsMatchedByCustomerHomeParty_CustomerHomePartyId(
            @Param("customerHomePartyId") Long customerHomePartyId,
            @Param("isMatched") Integer isMatched
    );

    Optional<PartySchedule> findByIdAndCustomerId(Long partyScheduleId, Long customerId);
}

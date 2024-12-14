package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.MyHomePartyDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerHomePartyRepository extends JpaRepository<CustomerHomeParty, Long>, CustomerHomPartyRepositoryCustom {

    @EntityGraph(attributePaths = {"partyServices"})
    CustomerHomeParty findByCustomerHomePartyId(@Param("id") Long customerHomePartyId);

    List<CustomerHomeParty> findByCustomerId(Long userId);

    Optional<CustomerHomeParty> findByCustomerHomePartyIdAndPartyStatusAndCustomerId(Long homePartyId, HomePartyStatus status, Long userId);

    @EntityGraph(attributePaths = {"customer"})
    @Query(
            """
        SELECT
            chp
        FROM CustomerHomeParty chp
            LEFT JOIN Customer AS c ON chp.customer.id = c.id
        WHERE chp.chef.id =:id
            AND chp.partyStatus = 'WAITING'
        ORDER BY chp.createdAt DESC
    """
    )
    List<CustomerHomeParty> findAllByChef_Id(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT new com.mayo.server.party.app.port.out.MyHomePartyDto(
        chp.partyInfo,
        k.address,
        chp.partySchedule,
        chp.adultCount,
        chp.childCount,
        chp.budget,
        chp.createdAt
    )
    FROM CustomerHomeParty AS chp
        INNER JOIN Kitchen AS k ON k.customer.id = chp.customer.id
    WHERE chp.customerHomePartyId IN :ids
        AND k.createdAt = (
                SELECT MAX(k2.createdAt)
                FROM Kitchen k2
                WHERE k2.customer.id = chp.customer.id
            )
    """)
    List<MyHomePartyDto> findAllByCustomerHomePartyId(@Param("ids") List<Long> ids);

    @Modifying
    @Query(
            "UPDATE CustomerHomeParty AS chp SET chp.partyStatus = :status WHERE chp.customerHomePartyId =:customerHomePartyId"
    )
    void updatePartyStatusByCustomerHomePartyId(
            @Param("customerHomePartyId") Long customerHomePartyId,
            @Param("status") HomePartyStatus status
    );

    Long countByCustomerIdAndPartyStatusAndPartyScheduleBetween(Long userId, HomePartyStatus finish, LocalDateTime startDate, LocalDateTime endDate);
}
package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.app.port.out.SingleKitchenToolsDto;
import com.mayo.server.customer.domain.model.KitchenTools;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KitchenToolsRepository extends JpaRepository<KitchenTools, Long> {

    @Query("""
    SELECT new com.mayo.server.customer.app.port.out.SingleKitchenToolsDto(
        kt.kitchenToolsId,
        kt.toolName
    ) FROM KitchenTools kt WHERE kt.kitchen.kitchenId = :kitchenId
    """)
    List<SingleKitchenToolsDto> findAllByKitchen_Id(@Param("kitchenId") Long kitchenId);
}

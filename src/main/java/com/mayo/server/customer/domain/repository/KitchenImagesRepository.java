package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.app.port.out.SingleKitchenImageDto;
import com.mayo.server.customer.domain.model.KitchenImages;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KitchenImagesRepository extends JpaRepository<KitchenImages, Long> {

    @Query(
            """
    SELECT new com.mayo.server.customer.app.port.out.SingleKitchenImageDto(
        ki.kitchenImagesId,
        ki.imageName,
        ki.order,
        ki.deletedAt
    ) FROM KitchenImages ki WHERE ki.kitchen.kitchenId =:kitchenId
    """
    )
    List<SingleKitchenImageDto> findAllByKitchen_KitchenId(@Param("kitchenId") Long kitchenId);
}

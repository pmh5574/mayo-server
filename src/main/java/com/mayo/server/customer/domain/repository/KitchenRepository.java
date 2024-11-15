package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.app.port.out.SingleKitchenDto;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Kitchen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KitchenRepository extends JpaRepository<Kitchen, Long>, KitchenRepositoryCustom {

    @Query("""
    SELECT new com.mayo.server.customer.app.port.out.SingleKitchenDto(
        k.kitchenId,
        k.nickName,
        k.address,
        k.addressSub,
        k.burnerType,
        k.burnerQuantity,
        k.additionalEquipment,
        k.requirements,
        k.considerations,
        k.deletedAt
    ) FROM Kitchen k WHERE k.customer.id = :customerId
    """)
    List<SingleKitchenDto> findAllByCustomer_Id(Long customerId);

    Optional<Kitchen> findByKitchenIdAndCustomerId(Long kitchenId, Long customerId);

    List<Kitchen> findByCustomerId(Long customerId);

    Optional<Kitchen> findByCustomerIdAndMainStatus(Long userId, KitchenMainStatus kitchenMainStatus);
}

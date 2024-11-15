package com.mayo.server.customer.domain.repository;

import static com.mayo.server.customer.domain.model.QKitchen.kitchen;
import static com.mayo.server.customer.domain.model.QKitchenImages.kitchenImages;

import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class KitchenRepositoryImpl implements KitchenRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<CustomerKitchenListDto> getKitchenAllList(final Long userId) {

        List<Kitchen> kitchens = jpaQueryFactory.select(kitchen)
                .from(kitchen)
                .leftJoin(kitchen.kitchenImagesList, kitchenImages).fetchJoin()
                .where(kitchen.customer.id.eq(userId))
                .fetch();

        return kitchens.stream()
                .map(kitchenData -> new CustomerKitchenListDto(
                        kitchenData.getKitchenId(),
                        kitchenData.getMainStatus(),
                        kitchenData.getNickName(),
                        kitchenData.getAddress(),
                        kitchenData.getAddressSub(),
                        kitchenData.getKitchenImagesList().stream()
                                .map(KitchenImages::getImageName)
                                .toList()
                ))
                .toList();
    }
}

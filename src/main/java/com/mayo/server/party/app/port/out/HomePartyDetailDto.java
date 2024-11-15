package com.mayo.server.party.app.port.out;

import com.mayo.server.customer.app.port.out.SingleKitchenImageDto;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.CustomerHomePartyServices;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class HomePartyDetailDto {

    private CustomerHomeParty customerHomeParty;
    private String address;
    private String burnerType;
    private  List<SingleKitchenImageDto> kitchenImages;
    private String kitchenRequirements;
    private String kitchenConsiderations;
    private List<String> kitchenTools;

}

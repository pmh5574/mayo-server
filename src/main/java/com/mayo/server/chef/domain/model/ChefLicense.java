package com.mayo.server.chef.domain.model;

import com.mayo.server.chef.app.port.in.TransformedImageDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "chef_license")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefLicense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chefId;

    private String chefLicense;

    @Column(name = "`order`")
    private Integer order;

    private String chefLicenceImage;

    private String deletedAt;

    public static List<TransformedImageDto> getLicenses(
            List<ChefLicense> licenses
    ) {

        return licenses.stream().map(v -> {

            return new TransformedImageDto(
                    v.getOrder(),
                    v.getChefLicenceImage()
            );
        }).toList();
    }
    
}

package com.mayo.server.chef.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@Table(name = "chef_information")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefInformation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chefId;

    private String chefInfoExperience;

    private String chefInfoIntroduce;

    private String chefInfoRegion;

    private String chefInfoAdditional;

    private String chefInfoDescription;

    @Column(name = "chef_info_hope_pay")
    private Long hopePay;

    @Column(name = "chef_info_min_time")
    private Integer minServiceTime;

    public String getChefInfoExperienceOrDefault() {
        return Optional.ofNullable(this.chefInfoExperience)
                .orElse("");
    }

    public String getChefInfoIntroduceOrDefault() {
        return Optional.ofNullable(this.chefInfoIntroduce)
                .orElse("");
    }

    public String getChefInfoRegionOrDefault() {
        return Optional.ofNullable(this.chefInfoRegion)
                .orElse("");
    }

    public String getChefInfoDescriptionOrDefault() {
        return Optional.ofNullable(this.chefInfoDescription)
                .orElse("");
    }

    public String getChefInfoAdditionalOrDefault() {
        return Optional.ofNullable(chefInfoAdditional)
                .orElse("");
    }

    public Long getHopePayOrDefault() {
        return Optional.ofNullable(hopePay)
                .orElse(0L);
    }

    public Integer getMinServiceTimeOrDefault() {
        return Optional.ofNullable(minServiceTime)
                .orElse(0);
    }

}

package com.mayo.server.chef.domain.model;

import com.mayo.server.party.domain.model.PartySchedule;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "chef")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chef {

    @Id @Tsid
    @Column(name = "id")
    private Long id;

    private String chefUsername;

    private String chefPassword;

    private String chefName;

    private String chefBirthday;

    private String chefPhone;

    private String chefEmail;

    private String createdAt;

    private String deletedAt;

    private Integer isApproved;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL)
    private List<PartySchedule> partySchedules = new ArrayList<>();

}

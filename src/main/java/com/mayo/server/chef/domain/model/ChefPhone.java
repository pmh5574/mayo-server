package com.mayo.server.chef.domain.model;

import com.mayo.server.common.enums.VerifyCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "chef_phone")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    @Column(name = "authnum")
    private String authNum;

    @Column(
            name =  "is_verified"
    )
    private Integer isVerified;

    private String type;

    private String createdAt;

    private String deletedAt;
}

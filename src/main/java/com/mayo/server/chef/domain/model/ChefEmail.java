package com.mayo.server.chef.domain.model;

import com.mayo.server.common.enums.VerifyCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "chef_email")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefEmail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String type;

    @Column(name = "authnum")
    private String authNum;

    private Integer isVerified;

    private String createdAt;

    private String deletedAt;
}

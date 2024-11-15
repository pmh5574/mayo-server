package com.mayo.server.account.domain.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Builder
@Table(name = "chef_account")
@AllArgsConstructor
@Entity
public class ChefAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chefId;

    private String bank;

    private String account;

    private String createdAt;
}

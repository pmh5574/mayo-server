package com.mayo.server.chef.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "chef_service")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chefId;

    @Column(name = "service_name")
    private String serviceName;

    private String createdAt;

}

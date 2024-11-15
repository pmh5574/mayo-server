package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationJpaRepository extends JpaRepository<ChefInformation, Long> {

    ChefInformation findByChefId(Long id);
}

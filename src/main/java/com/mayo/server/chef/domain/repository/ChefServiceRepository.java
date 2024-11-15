package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChefServiceRepository extends JpaRepository<ChefService, Long> {

    @Query("""
    SELECT cs.serviceName FROM ChefService cs WHERE cs.chefId = :id
    """)
    List<String> findAllServiceNames(@Param("id") Long id);

    void deleteByChefId(Long chefId);
}

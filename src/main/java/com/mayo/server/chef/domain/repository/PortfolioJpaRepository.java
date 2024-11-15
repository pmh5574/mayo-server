package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefPortfolio;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioJpaRepository extends JpaRepository<ChefPortfolio, Long> {

    List<ChefPortfolio> findAllByChefId(Long id);

    @Modifying
    @Query("UPDATE ChefPortfolio AS cp SET cp.deletedAt =:deletedAt WHERE cp.chefId = :id")
    void deleteById(
            @Param("id") Long id,
            @Param("deletedAt") String deletedAt
    );

}

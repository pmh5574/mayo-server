package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefLicense;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LicenseJpaRepository extends JpaRepository<ChefLicense, Long> {

    List<ChefLicense> findAllByChefId(Long id);

    @Modifying
    @Query("UPDATE ChefLicense AS cl SET cl.deletedAt =:deletedAt WHERE cl.chefId = :id")
    void deleteById(
            @Param("id") Long id,
            @Param("deletedAt") String deletedAt
    );

}

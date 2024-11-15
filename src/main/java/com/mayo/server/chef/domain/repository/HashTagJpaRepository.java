package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagJpaRepository extends JpaRepository<ChefHashTag, Long> {

    List<ChefHashTag> findAllByChefId(Long id);

    @Modifying
    @Query("DELETE FROM ChefHashTag AS cht WHERE cht.chefId = :chefId")
    void deletedByChefId(@Param("chefId") Long chefId);
}

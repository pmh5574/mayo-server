package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.app.port.out.ChefUsernameQuery;
import com.mayo.server.chef.domain.model.Chef;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChefJpaRepository extends JpaRepository<Chef, Long>, ChefJpaRepositoryCustom {

    Boolean existsByChefUsername(String username);

    Chef findByChefUsername(String username);

    Chef findChefByChefPhone(String phone);

    Chef findChefByChefEmail(String email);

    @Query("SELECT c.chefUsername FROM Chef AS c WHERE c.chefPhone = :phone")
    ChefUsernameQuery getUsernameByPhone(@Param("phone") String phone);

    Chef getChefByChefUsernameAndChefNameAndChefBirthdayAndChefPhone(
            String username,
            String name,
            String birthday,
            String phone
    );

    @Modifying
    @Query("UPDATE Chef AS c SET c.chefPassword = :password WHERE c.chefUsername = :username")
    void updateChefPwd(
            @Param("username") String username,
            @Param("password") String password
    );

    @Modifying
    @Query("UPDATE Chef AS c SET c.chefName = :name, c.chefBirthday = :birthday WHERE c.id = :id")
    void updateChefInfoNormal(
            @Param("name") String name,
            @Param("birthday") String birthday,
            @Param("id") Long id
    );

    @Modifying
    @Query("UPDATE Chef AS c SET c.chefName = :name, c.chefBirthday = :birthday, c.chefPhone =:phone WHERE c.id = :id")
    void updateChefInfoPhone(
            @Param("name") String name,
            @Param("birthday") String birthday,
            @Param("phone") String phone,
            @Param("id") Long id
    );

    @Modifying
    @Query("UPDATE Chef AS c SET c.chefName = :name, c.chefBirthday = :birthday, c.chefEmail =:email WHERE c.id = :id")
    void updateChefInfoEmail(
            @Param("name") String name,
            @Param("birthday") String birthday,
            @Param("email") String email,
            @Param("id") Long id
    );

    @Modifying
    @Query("UPDATE Chef AS c SET c.chefName = :name, c.chefBirthday = :birthday, c.chefEmail =:email, c.chefPhone =:phone WHERE c.id = :id")
    void updateChefInfoAll(
            @Param("name") String name,
            @Param("birthday") String birthday,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("id") Long id
    );

}

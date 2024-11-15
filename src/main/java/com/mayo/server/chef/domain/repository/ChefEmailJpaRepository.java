package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.common.enums.VerifyCode;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChefEmailJpaRepository extends JpaRepository<ChefEmail, Long> {

    ChefEmail findChefEmailByEmail(String email);

    ChefEmail findByEmailAndType(String email, String type);

    Boolean existsByEmailAndTypeAndIsVerified(String phone, String type, Integer isVerified);

    @Query("""

            SELECT ce.id
            FROM ChefEmail AS ce
            INNER JOIN Chef AS c ON ce.email = c.chefEmail
            WHERE ce.isVerified = :isVerified
                AND ce.email = :email
                AND ce.type = :type
                AND c.chefName = :name
            """)
    Long existsByEmailAndTypeAndIsVerified(
            @Param("email") String email,
            @Param("name") String name,
            @Param("type") String type,
            @Param("isVerified") Integer isVerified);

    @Modifying
    @Query("UPDATE ChefEmail AS ce SET ce.isVerified = 1 WHERE ce.email = :email")
    void updateChefEmailByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE ChefEmail AS ce SET ce.isVerified = 0, ce.authNum =:authNum, ce.createdAt = :createdAt WHERE ce.email = :email AND ce.type = 'REGISTER'")
    void updateResendEmailByRegister(
            @Param("email") String email,
            @Param("authNum") String authNum,
            @Param("createdAt") String createdAt
    );

    @Modifying
    @Query("UPDATE ChefEmail AS ce SET ce.isVerified = 0, ce.authNum =:authNum, ce.createdAt = :createdAt WHERE ce.email = :email AND ce.type = 'USERNAME'")
    void updateResendEmailByFindUsername(
            @Param("email") String email,
            @Param("authNum") String authNum,
            @Param("createdAt") String createdAt
    );

    @Modifying
    @Query("UPDATE ChefEmail AS ce SET ce.isVerified = 0, ce.authNum =:authNum, ce.createdAt = :createdAt WHERE ce.email = :email AND ce.type = 'PWD'")
    void updateResendEmailByFindPwd(
            @Param("email") String email,
            @Param("authNum") String authNum,
            @Param("createdAt") String createdAt
    );
}

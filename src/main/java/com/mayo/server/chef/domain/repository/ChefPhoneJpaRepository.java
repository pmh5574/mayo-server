package com.mayo.server.chef.domain.repository;

import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.common.enums.VerifyCode;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChefPhoneJpaRepository extends JpaRepository<ChefPhone, Long> {

    ChefPhone findByPhone(String phone);

    ChefPhone findByPhoneAndType(String phone, String type);

    Boolean existsByPhoneAndTypeAndIsVerified(String phone, String type, Integer isVerified);

    @Query("""

            SELECT cf.id
            FROM ChefPhone AS cf
            INNER JOIN Chef AS c ON cf.phone = c.chefPhone
            WHERE cf.isVerified = :isVerified
                AND cf.phone = :phone
                AND cf.type = :type
                AND c.chefName = :name
            """)
    Long existsByPhoneAndTypeAndIsVerified(
            @Param("phone") String phone,
            @Param("name") String name,
            @Param("type") String type,
            @Param("isVerified") Integer isVerified);

    @Modifying
    @Query("UPDATE ChefPhone AS cf SET cf.isVerified = 1 WHERE cf.phone = :phone")
    void updatePhoneVerifyByPhone(@Param("phone") String phone);

    @Modifying
    @Query("UPDATE ChefPhone AS cf SET cf.authNum = :authNum, cf.isVerified = 0, cf.createdAt =:createdAt WHERE cf.id = :id")
    void updateResendByFindUsername(
            @Param("id") Long id,
            @Param("authNum") String authNum,
            @Param("createdAt") String createdAt
    );

    @Modifying
    @Query("UPDATE ChefPhone AS cf SET cf.authNum = :authNum, cf.isVerified = 0, cf.createdAt =:createdAt WHERE cf.id = :id")
    void updateResendByFindPwd(
            @Param("id") Long id,
            @Param("authNum") String authNum,
            @Param("createdAt") String createdAt
    );
}

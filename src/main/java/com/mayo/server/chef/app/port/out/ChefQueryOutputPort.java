package com.mayo.server.chef.app.port.out;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.common.enums.VerifyCode;

import java.util.Optional;

public interface ChefQueryOutputPort {

    Boolean existsByChefUsername(String username);

    Boolean existsByPhoneRegister(String phone, String type, Integer isVerified);

    Long existsByPhoneRegister(String phone, String name, String type, Integer isVerified);

    Boolean existsByEmailRegister(String email, String type, Integer isVerified);

    Long existsByEmailRegister(String email, String name, String type, Integer isVerified);

    Chef getChefByUsernameAndNameAndBirthdayAndPhone(
            String username,
            String name,
            String birthday,
            String phone
    );

    Chef getChefById(String id);

    Chef getChefByUsername(String username);

    Chef getChefByPhone(String phone);

    Chef getChefByEmail(String email);

    ChefUsernameQuery getUsername(String phone);

    ChefPhone getPhoneVerify(String phone);

    ChefPhone getPhoneVerifyByType(String phone, String type);

    ChefEmail getEmailVerify(String email);

    ChefEmail getEmailVerifyByType(String email, String type);


}

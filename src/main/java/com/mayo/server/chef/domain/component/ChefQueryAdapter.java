package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.app.port.out.ChefUsernameQuery;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.chef.domain.repository.ChefEmailJpaRepository;
import com.mayo.server.chef.domain.repository.ChefJpaRepository;
import com.mayo.server.chef.domain.repository.ChefPhoneJpaRepository;
import com.mayo.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ChefQueryAdapter implements ChefQueryOutputPort {

    private final ChefJpaRepository chefJpaRepository;
    private final ChefPhoneJpaRepository phoneJpaRepository;
    private final ChefEmailJpaRepository emailJpaRepository;

    @Override
    public Boolean existsByChefUsername(String username) {
        return chefJpaRepository.existsByChefUsername(username);
    }

    @Override
    public Boolean existsByPhoneRegister(String phone, String type, Integer isVerified) {

        return phoneJpaRepository.existsByPhoneAndTypeAndIsVerified(phone, type, isVerified);
    }

    @Override
    public Long existsByPhoneRegister(String phone, String name, String type, Integer isVerified) {
        return phoneJpaRepository.existsByPhoneAndTypeAndIsVerified(phone, name, type, isVerified);
    }

    @Override
    public Boolean existsByEmailRegister(String email, String type, Integer isVerified) {
        return emailJpaRepository.existsByEmailAndTypeAndIsVerified(email, type, isVerified);
    }

    @Override
    public Long existsByEmailRegister(String email, String name, String type, Integer isVerified) {
        return emailJpaRepository.existsByEmailAndTypeAndIsVerified(email, name, type, isVerified);
    }

    @Override
    public Chef getChefByUsernameAndNameAndBirthdayAndPhone(String username, String name, String birthday, String phone) {
        return chefJpaRepository.getChefByChefUsernameAndChefNameAndChefBirthdayAndChefPhone(
                username, name, birthday, phone);
    }

    @Override
    public Chef getChefById(String id) {
        return chefJpaRepository.findById(Long.parseLong(id)).orElse(null);

    }

    @Override
    public Chef getChefByUsername(String username) {
        return chefJpaRepository.findByChefUsername(username);
    }

    @Override
    public Chef getChefByPhone(String phone) {
        return chefJpaRepository.findChefByChefPhone(phone);
    }

    @Override
    public Chef getChefByEmail(String email) {
        return chefJpaRepository.findChefByChefEmail(email);
    }

    @Override
    public ChefUsernameQuery getUsername(String phone) {
        return chefJpaRepository.getUsernameByPhone(phone);
    }

    @Override
    public ChefPhone getPhoneVerify(String phone) {
        return phoneJpaRepository.findByPhone(phone);
    }

    @Override
    public ChefPhone getPhoneVerifyByType(String phone, String type) {
        return phoneJpaRepository.findByPhoneAndType(phone, type);
    }

    @Override
    public ChefEmail getEmailVerify(String email) {
        return emailJpaRepository.findChefEmailByEmail(email);
    }

    @Override
    public ChefEmail getEmailVerifyByType(String email, String type) {
        return emailJpaRepository.findByEmailAndType(email, type);
    }


}

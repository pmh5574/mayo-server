package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import com.mayo.server.customer.domain.model.Customer;

public interface CustomerQueryInputPort {
    Customer findByCustomerUsernameAndCustomerPassword(String username, String hashedPwd);

    boolean findByPhoneAndCheckRegister(String phone);

    boolean findByUsernameAndCheckRegister(String userId);

    Long postRegisterByPhone(CustomerPhoneRegisterServiceRequest customerPhoneRegisterRequest);

    boolean findByEmailAndCheckRegister(String email);

    Long postRegisterByEmail(CustomerEmailRegisterServiceRequest request);

    void checkIdFindByPhone(String phone);

    Customer checkIdFindByPhoneAndName(String phone, String name);

    void checkIdFindByEmail(String email);

    Customer checkIdFindByEmailAndName(String email, String name);

    Customer checkPasswordFindByUserIdAndPhoneAndName(String username, String phone, String name, String birthday);

    Customer checkPasswordFindByUserIdAndPhone(String username, String phone);

    void updatePassword(Customer customer, String password);

    Customer findByUsername(String username);

    Customer checkPasswordFindByUserIdAndEmailAndName(String username, String email, String name, String birthday);

    Customer checkPasswordFindByUserIdAndEmail(String username, String email);

    Customer findById(Long customerId);
}

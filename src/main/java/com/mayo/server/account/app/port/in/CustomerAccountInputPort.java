package com.mayo.server.account.app.port.in;

import com.mayo.server.account.domain.models.CustomerAccount;
import com.mayo.server.customer.domain.model.Customer;

public interface CustomerAccountInputPort {

    Customer getCustomer(Long userId);

    Long postAccount(CustomerAccount customerAccount);

    CustomerAccount getAccount(Long userId);

    boolean checkAccountExists(Long userId);
}

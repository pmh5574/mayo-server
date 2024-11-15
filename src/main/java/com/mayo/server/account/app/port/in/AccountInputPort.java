package com.mayo.server.account.app.port.in;

public interface AccountInputPort  {

    void saveBank(Long id, String bank);

    void saveAccount(Long id, String account);
}

package com.jpmc.cib.homework.core;

import java.util.List;
import java.util.Optional;

public class AccountValidationRequestBody {
    private String accountNumber;
    private List<String> providers;


    public AccountValidationRequestBody(String accountNumber, List<String> providers) {
        this.accountNumber = accountNumber;
        this.providers = providers;
    }

    public AccountValidationRequestBody() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        this.providers = providers;
    }
}

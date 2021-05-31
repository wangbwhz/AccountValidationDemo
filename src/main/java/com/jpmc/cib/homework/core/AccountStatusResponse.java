package com.jpmc.cib.homework.core;

import java.util.List;

public class AccountStatusResponse {
    private List<AccountStatus> result;

    public AccountStatusResponse(List<AccountStatus> result) {
        this.result = result;
    }

    public AccountStatusResponse() {
    }

    public List<AccountStatus> getResult() {
        return result;
    }

    public void setResult(List<AccountStatus> result) {
        this.result = result;
    }
}

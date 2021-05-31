package com.jpmc.cib.homework.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountStatus {

    private boolean isValid;
    private String provider;

    public AccountStatus() {
    }

    public AccountStatus(boolean isValid, String provider) {
        this.isValid = isValid;
        this.provider = provider;
    }
    @JsonProperty("isValid")
    public boolean isValid() {
        return isValid;
    }
    @JsonProperty("isValid")
    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "AccountStatus{" +
                "isValid=" + isValid +
                ", provider='" + provider + '\'' +
                '}';
    }
}

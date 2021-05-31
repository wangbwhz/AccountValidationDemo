package com.jpmc.cib.homework.core;

public class AccountStatus {

    private boolean isValid;
    private String provider;

    public AccountStatus() {
    }

    public AccountStatus(boolean isValid, String provider) {
        this.isValid = isValid;
        this.provider = provider;
    }

    public boolean isValid() {
        return isValid;
    }

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

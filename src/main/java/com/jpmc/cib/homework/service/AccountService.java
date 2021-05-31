package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.exceptions.DataProviderException;

import java.util.List;

public interface AccountService {
    /**
     * Validate account
     * @param accountNumber account number
     * @return account status
     * @throws DataProviderException exception throw during validation
     */
    List<AccountStatus> validateAccountByProviderNames(String accountNumber) throws DataProviderException;

    /**
     * Validate account by provider name provided
     * @param accountNumber account number
     * @param dataProviderNames data provider names
     * @return account status
     * @throws DataProviderException exception throw during validation
     */
    List<AccountStatus> validateAccountByProviderNames(String accountNumber,List<String> dataProviderNames) throws DataProviderException;

}

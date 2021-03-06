package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
import com.jpmc.cib.homework.exceptions.DataProviderException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DataProviderService {
    /**
     * Get providers by provider names
     * @param providerNames provider Names
     * @return selected data providers
     * @throws DataProviderException data provider is invalid
     */
     List<DataProvider> getProvidersByProviderNames(List<String> providerNames) throws DataProviderException;

    /**
     * Get all providers in the system
     * @return all providers
     * @throws DataProviderException No data providers are available
     */
     List<DataProvider> getAllProvidersByProviderNames() throws DataProviderException;

    /**
     * Validate Account By Provider
     * @param accountNumber number of the account
     * @param dataProvider data provider
     * @return account status
     * @throws DataProviderException exception of validation
     */
     CompletableFuture<AccountStatus> validateAccountByProvider(String accountNumber, DataProvider dataProvider) throws DataProviderException;

    }

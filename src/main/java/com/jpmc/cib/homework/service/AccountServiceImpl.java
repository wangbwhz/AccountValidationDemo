package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
import com.jpmc.cib.homework.exceptions.AccountException;
import com.jpmc.cib.homework.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    Logger LOG= LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    DataProviderService dataProviderService;

    private List<AccountStatus> validateAccountByProviders(String accountNumber, List<DataProvider> selectedDataProviders) throws AccountException {
        CompletableFuture<AccountStatus>[] accountStatusCompletableFutures = new CompletableFuture[selectedDataProviders.size()];
        try {
            for (int i = 0; i < selectedDataProviders.size(); i++) {
                accountStatusCompletableFutures[i] = dataProviderService.validateAccountByProvider(accountNumber, selectedDataProviders.get(i));
            }
        } catch (DataProviderException e) {
            LOG.error("Failed to validate account, request account number: {}, selectedDataProviders {}",accountNumber,selectedDataProviders,e);
            throw new AccountException("Error occurred during account validation process");
        }

        try {
            CompletableFuture.allOf(accountStatusCompletableFutures).get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            boolean validationSucceedExists=false;
            for (int i = 0; i < accountStatusCompletableFutures.length; i++) {
                CompletableFuture<AccountStatus> accountStatusCompletableFuture = accountStatusCompletableFutures[i];
                if (accountStatusCompletableFuture.isDone()) {
                    validationSucceedExists=true;
                    continue;
                }
                final DataProvider dataProvider=selectedDataProviders.get(i);
                accountStatusCompletableFuture.handle((t, ex) -> {
                    if (ex != null) {
                        if (e instanceof ExecutionException) {
                            LOG.error("Validation failed due execution for data provider name {} url {} due to execution failure", dataProvider.getName(), dataProvider.getUrl(),e);
                        } else  {
                            LOG.error("Validation failed due to timeout for data provider name {} url {} due to timeout", dataProvider.getName(),dataProvider.getUrl(),e);
                        }
                    }
                    return null;
                });

                //when all providers failed to validate account
                if(!validationSucceedExists){
                    LOG.error("Failed to validate account through any provider, request account number: {}, selectedDataProviders {}",accountNumber,selectedDataProviders,e);
                    throw new AccountException("Data providers are not available");
                }
            }
        }
        return Arrays.stream(accountStatusCompletableFutures).filter(f -> f.isDone() && !f.isCompletedExceptionally()).map(CompletableFuture::join).collect(Collectors.toList());
    }

    @Override
    public List<AccountStatus> validateAccountByProviderNames(String accountNumber) throws AccountException {
        List<DataProvider> selectedDataProviders;
        try {
            selectedDataProviders = dataProviderService.getAllProvidersByProviderNames();
        } catch (DataProviderException e) {
            LOG.error("Failed to get providers by provider names, request account number: {},",accountNumber,e);
            throw new AccountException("Data providers are not available");
        }
        return this.validateAccountByProviders(accountNumber,selectedDataProviders);
    }
    @Override
    public List<AccountStatus> validateAccountByProviderNames(String accountNumber,List<String> dataProviderNames) throws AccountException {
        List<DataProvider> selectedDataProviders;
        try {
            selectedDataProviders = dataProviderService.getProvidersByProviderNames(dataProviderNames);
        } catch (DataProviderException e) {
            LOG.error("Failed to get providers by provider names {} , request account number: {},",dataProviderNames,accountNumber,e);
            throw new AccountException("Data providers are not available");
        }
        return this.validateAccountByProviders(accountNumber,selectedDataProviders);
    }
}

package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
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

    private List<AccountStatus> validateAccountByProviders(String accountNumber, List<DataProvider> selectedDataProviders) throws DataProviderException {
        CompletableFuture<AccountStatus>[] accountStatusCompletableFutures = new CompletableFuture[selectedDataProviders.size()];
        for (int i = 0; i < selectedDataProviders.size(); i++) {
            accountStatusCompletableFutures[i] = dataProviderService.validateAccountByProvider(accountNumber, selectedDataProviders.get(i));
        }
        try {
            CompletableFuture.allOf(accountStatusCompletableFutures).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new DataProviderException("Error occurred during account validation process");
        } catch (Exception e) {
            for (int i = 0; i < accountStatusCompletableFutures.length; i++) {
                CompletableFuture<AccountStatus> accountStatusCompletableFuture = accountStatusCompletableFutures[i];
                if (accountStatusCompletableFuture.isDone()) continue;
                if (e instanceof ExecutionException) {
                    LOG.error("Validation failed for data provider name {} url {} due to execution failure", selectedDataProviders.get(i).getName(), selectedDataProviders.get(i).getUrl());
                } else if (e instanceof TimeoutException) {
                    LOG.error("Validation failed for data provider name {} url {} due to timeout", selectedDataProviders.get(i).getName(), selectedDataProviders.get(i).getUrl());
                }
            }
        }
        return Arrays.stream(accountStatusCompletableFutures).filter(f -> f.isDone() && !f.isCompletedExceptionally()).map(CompletableFuture::join).collect(Collectors.toList());
    }

    @Override
    public List<AccountStatus> validateAccountByProviderNames(String accountNumber) throws DataProviderException {
        List<DataProvider> selectedDataProviders = dataProviderService.getAllProvidersByProviderNames();
        return this.validateAccountByProviders(accountNumber,selectedDataProviders);
    }
    @Override
    public List<AccountStatus> validateAccountByProviderNames(String accountNumber,List<String> dataProviderNames) throws DataProviderException {
        List<DataProvider> selectedDataProviders = dataProviderService.getProvidersByProviderNames(dataProviderNames);
        return this.validateAccountByProviders(accountNumber,selectedDataProviders);
    }
}

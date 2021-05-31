package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @TestConfiguration
    static class AccountServiceTestConfiguration {
        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl();
        }
    }
    private final String providerName = "provider1";
    private final String timeoutProviderName = "provider2";
    private final String accountNumber = "1234567";
    private final String url = "http://url.com";
    private final String timeoutUrl = "http://url.com";

    private final String provider="provider1";
    private final boolean isValid=true;
    private final List<DataProvider> providers = new ArrayList<>();
    private final List<DataProvider> providersWithOneTimeOutDataProvider = new ArrayList<>();

    private DataProvider dataProvider=new DataProvider(providerName,url);
    private DataProvider timeoutDataProvider=new DataProvider(timeoutProviderName,timeoutUrl);

    private final List<String> providersWithOneTimeOutProviderNames=new ArrayList<>();
    private final List<String> validProviderNames=new ArrayList<>();
    @Autowired
    AccountServiceImpl accountService;
    @MockBean
    DataProviderServiceImpl dataProviderService;
    @Before
    public void setup() {
        providers.add(dataProvider);
        providersWithOneTimeOutDataProvider.add(dataProvider);
        providersWithOneTimeOutDataProvider.add(timeoutDataProvider);
        validProviderNames.add(providerName);
        providersWithOneTimeOutProviderNames.add(providerName);
        providersWithOneTimeOutProviderNames.add(timeoutProviderName);
    }

    @Test
    public void validateAccountByProviderNamesSuccess() throws Exception {
        CompletableFuture<AccountStatus> future = supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new AccountStatus(isValid,provider);
        });
        Mockito.when(dataProviderService.getAllProvidersByProviderNames()).thenReturn(providers);
        Mockito.when(dataProviderService.validateAccountByProvider(accountNumber,dataProvider)).thenReturn(future);
        List<AccountStatus> accountStatusAns=accountService.validateAccountByProviderNames(accountNumber);
        assertEquals(1,accountStatusAns.size());
        assertEquals(providerName,accountStatusAns.get(0).getProvider());
        assertTrue(accountStatusAns.get(0).isValid());
    }
    @Test
    public void validateAccountByProviderNamesProvidedSuccess() throws Exception {
        CompletableFuture<AccountStatus> future = supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new AccountStatus(isValid,provider);
        });
        Mockito.when(dataProviderService.getProvidersByProviderNames(validProviderNames)).thenReturn(providers);
        Mockito.when(dataProviderService.validateAccountByProvider(accountNumber,dataProvider)).thenReturn(future);
        List<AccountStatus> accountStatusAns=accountService.validateAccountByProviderNames(accountNumber,validProviderNames);
        assertEquals(1,accountStatusAns.size());
        assertEquals(providerName,accountStatusAns.get(0).getProvider());
        assertTrue(accountStatusAns.get(0).isValid());
    }

    @Test
    public void validateAccountByProviderNamesOneTimeoutOneSucceed() throws Exception {
        CompletableFuture<AccountStatus> future= supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new AccountStatus(isValid,provider);
        });
        CompletableFuture<AccountStatus> future2 = supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new AccountStatus(isValid,timeoutProviderName);
        });
        Mockito.when(dataProviderService.getAllProvidersByProviderNames()).thenReturn(providersWithOneTimeOutDataProvider);
        Mockito.when(dataProviderService.validateAccountByProvider(accountNumber,dataProvider)).thenReturn(future);
        Mockito.when(dataProviderService.validateAccountByProvider(accountNumber,timeoutDataProvider)).thenReturn(future2);
        List<AccountStatus> accountStatusAns=accountService.validateAccountByProviderNames(accountNumber);
        assertEquals(1,accountStatusAns.size());
        assertEquals(providerName,accountStatusAns.get(0).getProvider());
        assertTrue(accountStatusAns.get(0).isValid());
    }

}

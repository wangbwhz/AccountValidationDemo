package com.jpmc.cib.homework.service;
import com.jpmc.cib.homework.configuration.DataProviderProperties;
import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
import com.jpmc.cib.homework.exceptions.DataProviderException;
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
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class DataProviderServiceTest {
    @TestConfiguration
    static class DataProviderServiceConfiguration {
        @Bean
        public DataProviderService dataProviderService() {
            return new DataProviderServiceImpl();
        }
    }

    @Autowired
    DataProviderServiceImpl dataProviderService;
    @MockBean
    DataProviderProperties dataProviderProperties;
    private final String providerName = "provider1";
    private final String accountNumber = "1234567";
    private final String invalidProviderName = "dummy";
    private final String url = "http://url.com";
    private final List<DataProvider> providers = new ArrayList<>();
    private final List<String> allProviderNames=new ArrayList<>();
    private final List<String> validProviderNames=new ArrayList<>();
    private final List<String> inValidProviderNames=new ArrayList<>();
    private DataProvider dataProvider=new DataProvider(providerName,url);
    private final List<DataProvider> emptyProviders=new ArrayList<>();

    @Before
    public void setup() {
        providers.add(new DataProvider(providerName, url));
        allProviderNames.add(providerName);
        allProviderNames.add(invalidProviderName);
        validProviderNames.add(providerName);
    }

    @Test
    public void getAllProvidersByProviderNamesSuccess() throws Exception {
        Mockito.when(dataProviderProperties.getDataProviders()).thenReturn(providers);
        List<DataProvider> dataProvidersAns=dataProviderService.getAllProvidersByProviderNames();
        assertEquals(1,dataProvidersAns.size());
        assertEquals(providerName,dataProvidersAns.get(0).getName());
        assertEquals(url,dataProvidersAns.get(0).getUrl());

    }
    @Test(expected = DataProviderException.class)
    public void getAllProvidersByProviderNamesThrowsException() throws Exception {
        Mockito.when(dataProviderProperties.getDataProviders()).thenReturn(emptyProviders);
        dataProviderService.getAllProvidersByProviderNames();
    }
    @Test
    public void getProvidersByProviderNamesSuccess() throws Exception {
        DataProviderServiceImpl dataProviderService = spy(DataProviderServiceImpl.class);
        doReturn(providers).when(dataProviderService).getAllProvidersByProviderNames();
        List<DataProvider> dataProvidersAns=dataProviderService.getProvidersByProviderNames(allProviderNames);
        assertEquals(1,dataProvidersAns.size());
        assertEquals(providerName,dataProvidersAns.get(0).getName());
        assertEquals(url,dataProvidersAns.get(0).getUrl());
    }
    @Test(expected = DataProviderException.class)
    public void getProvidersByProviderNamesWhenNoDataProvidersThrowsException() throws Exception {
        DataProviderServiceImpl dataProviderService = spy(DataProviderServiceImpl.class);
        doThrow(DataProviderException.class).when(dataProviderService).getAllProvidersByProviderNames();
        List<DataProvider> dataProvidersAns=dataProviderService.getProvidersByProviderNames(allProviderNames);
    }
    @Test(expected = DataProviderException.class)
    public void getProvidersWhenNoAvailableDataProvidersByProviderNamesThrowsException() throws Exception {
        DataProviderServiceImpl dataProviderService = spy(DataProviderServiceImpl.class);
        doReturn(providers).when(dataProviderService).getAllProvidersByProviderNames();
        List<DataProvider> dataProvidersAns=dataProviderService.getProvidersByProviderNames(inValidProviderNames);
    }

    @Test
    public void queryAccountStatusByProviderSuccess() throws Exception {
        Mockito.when(dataProviderProperties.getDataProviders()).thenReturn(providers);
        AccountStatus accountStatusAns=dataProviderService.queryAccountStatusByProvider(accountNumber,dataProvider);
        assertEquals(providerName,accountStatusAns.getProvider());
        assertTrue(accountStatusAns.isValid());
    }
    @Test
    public void validateAccountByProviderSuccess() throws Exception {
        CompletableFuture<AccountStatus> accountStatusCompleted =dataProviderService.validateAccountByProvider(accountNumber,dataProvider);
        AccountStatus accountStatusAns=accountStatusCompleted.get();
        assertEquals(providerName,accountStatusAns.getProvider());
        assertTrue(accountStatusAns.isValid());
    }
}

package com.jpmc.cib.homework.service;

import com.jpmc.cib.homework.configuration.DataProviderProperties;
import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import com.jpmc.cib.homework.exceptions.DataProviderException;

import java.util.*;

@Service
@EnableConfigurationProperties(DataProviderProperties.class)
public class DataProviderServiceImpl implements  DataProviderService {
    @Autowired
    DataProviderProperties dataProviderProperties;

    @Override
    public List<DataProvider> getAllProvidersByProviderNames() throws DataProviderException {
        List<DataProvider> allDataProviders = dataProviderProperties.getDataProviders();
        if(allDataProviders.size()==0){
            throw new DataProviderException("Data provider is invalid");
        }
        return allDataProviders;
    }

    @Override
    public List<DataProvider> getProvidersByProviderNames(List<String> providerNames) throws DataProviderException {
        List<DataProvider> allDataProviders = this.getAllProvidersByProviderNames();
        Map<String,DataProvider> dataProviderMap=new HashMap<>();
        List<DataProvider> selectedDataProviders = new ArrayList<>();
        for(DataProvider d: allDataProviders){
            dataProviderMap.put(d.getName(),d);
        }
        for(String providerName:providerNames){
            if(dataProviderMap.containsKey(providerName)) selectedDataProviders.add(dataProviderMap.get(providerName));
        }
        if(selectedDataProviders.size()==0){
            throw new DataProviderException("Data provider is invalid");
        }
        return selectedDataProviders;
    }



}

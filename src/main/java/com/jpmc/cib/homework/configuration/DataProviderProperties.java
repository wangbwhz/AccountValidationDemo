package com.jpmc.cib.homework.configuration;

import com.jpmc.cib.homework.core.DataProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("data-provider-config")
public class DataProviderProperties {
    private List<DataProvider> dataProviders;

    public List<DataProvider> getDataProviders() {
        return dataProviders;
    }

    public void setDataProviders(List<DataProvider> dataProviders) {
        this.dataProviders = dataProviders;
    }
}

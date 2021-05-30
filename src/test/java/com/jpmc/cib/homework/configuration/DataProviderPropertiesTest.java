package com.jpmc.cib.homework.configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties(DataProviderProperties.class)
@SpringBootTest(classes = DataProviderPropertiesTest.class)
public class DataProviderPropertiesTest {
   @Autowired
   DataProviderProperties dataProviderProperties;

   @Test
    public void testDataProviderNameAndUrl(){
      assertNotNull(dataProviderProperties.getDataProviders());
      assertEquals(2,dataProviderProperties.getDataProviders().size());
      assertNotNull(dataProviderProperties.getDataProviders().get(0).getName());
      assertNotNull(dataProviderProperties.getDataProviders().get(0).getUrl());
      assertNotNull(dataProviderProperties.getDataProviders().get(1).getName());
      assertNotNull(dataProviderProperties.getDataProviders().get(1).getUrl());
   }
}

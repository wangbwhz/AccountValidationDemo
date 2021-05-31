package com.jpmc.cib.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.AccountValidationRequestBody;
import com.jpmc.cib.homework.service.AccountService;
import com.jpmc.cib.homework.service.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;
    private final String accountNumber = "1234567";
    @MockBean
    private AccountServiceImpl accountService;
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void givenAccountNumberAndProvidersWhenValidateAccountThenReturnAccountStatus()
            throws Exception {
        List<AccountStatus>  accountStatuses=new ArrayList<>();
        String providerName="provider1";
        accountStatuses.add(new AccountStatus(true,providerName));
        AccountValidationRequestBody accountValidationRequestBody = new AccountValidationRequestBody(accountNumber,new ArrayList<>());
        doReturn(accountStatuses).when(accountService).validateAccountByProviderNames(accountNumber);
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountValidationRequestBody))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isValid", is(true)))
                .andExpect(jsonPath("$[0].provider", is(providerName)));

    }
}

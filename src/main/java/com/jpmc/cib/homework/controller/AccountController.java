package com.jpmc.cib.homework.controller;

import com.jpmc.cib.homework.core.AccountStatus;
import com.jpmc.cib.homework.core.AccountStatusResponse;
import com.jpmc.cib.homework.core.AccountValidationRequestBody;
import com.jpmc.cib.homework.exceptions.AccountException;
import com.jpmc.cib.homework.service.AccountService;
import com.jpmc.cib.homework.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    Logger LOG= LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountService accountService;
    @PostMapping("/accounts")
    ResponseEntity<AccountStatusResponse> validateAccounts(@RequestBody AccountValidationRequestBody accountValidationRequestBody) throws AccountException {
        if( accountValidationRequestBody.getProviders().size()>0){
            return ResponseEntity.ok(new AccountStatusResponse(accountService.validateAccountByProviderNames(accountValidationRequestBody.getAccountNumber(),accountValidationRequestBody.getProviders())));
        }else{
            return ResponseEntity.ok(new AccountStatusResponse(accountService.validateAccountByProviderNames(accountValidationRequestBody.getAccountNumber())));
        }
    }

}


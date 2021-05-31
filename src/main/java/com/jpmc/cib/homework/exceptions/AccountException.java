package com.jpmc.cib.homework.exceptions;

public class AccountException extends Exception{
    public AccountException(){

    }
    public AccountException(String message,Exception e){
        super(message,e);
    }

    public AccountException(String message) {
        super(message);
    }

}

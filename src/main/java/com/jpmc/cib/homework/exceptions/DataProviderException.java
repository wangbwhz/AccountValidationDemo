package com.jpmc.cib.homework.exceptions;

public class DataProviderException extends Exception{
    public DataProviderException(){

    }
    public DataProviderException(String message,Exception e){
        super(message,e);
    }

    public DataProviderException(String message) {
        super(message);
    }

}

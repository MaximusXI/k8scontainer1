package com.example.entry.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Harshil Makwana
 */
public class ServiceException extends Exception{
    private final int statusCode;
    private final String file;
    private final String errorMessage;
    public ServiceException(HttpStatus statusCode, String file,String message){
        this.statusCode = statusCode.value();
        this.file = file;
        this.errorMessage = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFile(){
        return file;
    }
}

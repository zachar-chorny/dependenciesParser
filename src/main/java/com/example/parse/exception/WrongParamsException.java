package com.example.parse.exception;

public class WrongParamsException extends RuntimeException{

    public WrongParamsException(String message){
        super(message);
    }
}

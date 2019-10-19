package com.kjh.telegram.exception;

public class CaptionIsEmptyException extends RuntimeException{

    public CaptionIsEmptyException(String message){
        super(message);
    }
}

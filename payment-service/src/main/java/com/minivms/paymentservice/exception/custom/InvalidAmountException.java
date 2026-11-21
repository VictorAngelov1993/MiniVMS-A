package com.minivms.paymentservice.exception.custom;

public class InvalidAmountException extends RuntimeException{


    public InvalidAmountException(String message) {
        super(message);
    }

}

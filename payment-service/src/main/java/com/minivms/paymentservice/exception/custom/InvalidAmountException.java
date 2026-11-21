package com.minivms.paymentservice.exception.custom;

import java.math.BigDecimal;

public class InvalidAmountException extends RuntimeException{


    public InvalidAmountException(String message) {
        super(message);
    }

}

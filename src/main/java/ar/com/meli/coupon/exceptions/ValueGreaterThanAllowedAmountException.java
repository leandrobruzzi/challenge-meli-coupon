package ar.com.meli.coupon.exceptions;

public class ValueGreaterThanAllowedAmountException extends Exception{

    public ValueGreaterThanAllowedAmountException(String message) {
        super(message);
    }

}

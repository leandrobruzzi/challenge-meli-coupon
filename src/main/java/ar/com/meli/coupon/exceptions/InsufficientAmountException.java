package ar.com.meli.coupon.exceptions;

public class InsufficientAmountException extends Exception{

    public InsufficientAmountException() {
        super();
    }

    public InsufficientAmountException(String message) {
        super(message);
    }

    public InsufficientAmountException(String message, Throwable cause) {
        super(message, cause);
    }

}
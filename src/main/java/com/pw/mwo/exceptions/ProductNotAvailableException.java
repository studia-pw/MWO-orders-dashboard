package com.pw.mwo.exceptions;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException() {
        super("Product is not available right now");
    }
}

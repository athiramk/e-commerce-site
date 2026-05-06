package com.athiramk.ecommercesite.common.exception;


@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Authentication required. Please login to continue.");
    }
}
package com.athiramk.ecommercesite.common.exception;


@SuppressWarnings("serial")
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("You do not have permission to perform this action.");
    }
}
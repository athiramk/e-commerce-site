package com.athiramk.ecommercesite.common.exception;

@SuppressWarnings("serial")
public class PaymentException extends BusinessException {

    private final String orderId;

    public PaymentException(String message, String orderId) {
        super("PAYMENT_FAILED", message);
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
}
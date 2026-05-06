package com.athiramk.ecommercesite.common.exception;

@SuppressWarnings("serial")
public class InsufficientStockException extends BusinessException {

    private final String productName;
    private final int requestedQuantity;
    private final int availableQuantity;

    public InsufficientStockException(String productName,
                                      int requestedQuantity,
                                      int availableQuantity) {
        super("INSUFFICIENT_STOCK",
              String.format("Insufficient stock for '%s'. Requested: %d, Available: %d",
                            productName, requestedQuantity, availableQuantity));
        this.productName       = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getProductName()    { return productName; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
}
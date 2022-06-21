package ru.exception;

public class ShopUnitNotFoundException extends RuntimeException {
    public ShopUnitNotFoundException(String message) {
        super(message);
    }
}

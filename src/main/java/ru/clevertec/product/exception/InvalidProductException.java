package ru.clevertec.product.exception;

public class InvalidProductException extends RuntimeException{
    public InvalidProductException() {
        super("Invalid Product");
    }
}

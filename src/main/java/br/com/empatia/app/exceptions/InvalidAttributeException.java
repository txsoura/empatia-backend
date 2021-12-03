package br.com.empatia.app.exceptions;

public class InvalidAttributeException extends RuntimeException {
    public InvalidAttributeException(String message) {
        super(message);
    }
}

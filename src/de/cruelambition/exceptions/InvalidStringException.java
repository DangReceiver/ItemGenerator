package de.cruelambition.exceptions;

public class InvalidStringException
        extends RuntimeException {
    public InvalidStringException() {
    }

    public InvalidStringException(String message) {
        super(message);
    }

    public InvalidStringException(Throwable throwable) {
        super(throwable);
    }
}


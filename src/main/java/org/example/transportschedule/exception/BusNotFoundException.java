package org.example.transportschedule.exception;

public class BusNotFoundException extends RuntimeException {
    public BusNotFoundException(String message) {
        super(message);
    }
}

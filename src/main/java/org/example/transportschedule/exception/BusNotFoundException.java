package org.example.transportschedule.exception;

public class BusNotFoundException extends RuntimeException {
    public BusNotFoundException(long id) {
        super("Bus with id " + id + " not found");
    }
}

package org.example.transportschedule.exception;

public class TrainNotFoundException extends RuntimeException {
  public TrainNotFoundException(long id) {
    super("Train with id " + id + " not found");
  }
}

package de.mankianer.tresensystem.exeptions.order;

public class UserExistsException extends Exception {

  public UserExistsException(String message) {
    super("User exist: " + message);
  }

}

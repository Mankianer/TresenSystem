package de.mankianer.tresensystem.exeptions;

public class UserMissingException extends Exception {

  public UserMissingException(String username) {
    super("User wasn't found: " + username);
  }

}

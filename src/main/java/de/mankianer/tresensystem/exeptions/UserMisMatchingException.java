package de.mankianer.tresensystem.exeptions;

public class UserMisMatchingException extends Exception {

  public UserMisMatchingException(String username1, String username2) {
    super(String.format("User didn't match: %s != %s", username1, username2));
  }

}

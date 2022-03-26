package de.mankianer.tresensystem.exeptions.order;

import de.mankianer.tresensystem.security.entities.User;

public class UpdateNotAllowedByUser extends Exception {
  public UpdateNotAllowedByUser(User user) {
    super("Update not allowed by user: " + user.getUsername());
  }
}

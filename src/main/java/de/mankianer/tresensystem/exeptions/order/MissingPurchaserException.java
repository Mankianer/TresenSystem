package de.mankianer.tresensystem.exeptions.order;

import lombok.Data;

public class MissingPurchaserException extends Exception {

  public MissingPurchaserException(String message) {
    super(message);
  }
}


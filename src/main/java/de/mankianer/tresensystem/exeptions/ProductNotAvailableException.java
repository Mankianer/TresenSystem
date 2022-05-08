package de.mankianer.tresensystem.exeptions;

public class ProductNotAvailableException extends Exception {
  private static final long serialVersionUID = 1L;

  public ProductNotAvailableException(String message) {
    super(message);
  }
}

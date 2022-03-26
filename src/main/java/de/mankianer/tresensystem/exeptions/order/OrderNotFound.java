package de.mankianer.tresensystem.exeptions.order;

public class OrderNotFound extends Exception {

  public OrderNotFound(String message) {
    super("Order not found:" + message);
  }
}


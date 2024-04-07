package com.search.wiki.exceptions.customexceptions;

/** The type Database access exception. */
public class DatabaseAccessException extends RuntimeException {
  /**
   * Instantiates a new Database access exception.
   *
   * @param message the message
   */
  public DatabaseAccessException(String message) {
    super(message);
  }
}

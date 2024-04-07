package com.search.wiki.exceptions.customexceptions;

/** The type Duplicate entry exception. */
public class DuplicateEntryException extends RuntimeException {
  /**
   * Instantiates a new Duplicate entry exception.
   *
   * @param message the message
   */
  public DuplicateEntryException(String message) {
    super(message);
  }
}

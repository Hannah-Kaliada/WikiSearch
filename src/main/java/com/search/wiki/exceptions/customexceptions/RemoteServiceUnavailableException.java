package com.search.wiki.exceptions.customexceptions;

/** The type Remote service unavailable exception. */
public class RemoteServiceUnavailableException extends RuntimeException {
  /**
   * Instantiates a new Remote service unavailable exception.
   *
   * @param message the message
   */
  public RemoteServiceUnavailableException(String message) {
    super(message);
  }
}

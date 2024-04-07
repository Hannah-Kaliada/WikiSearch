package com.search.wiki.exceptions.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** The type Resource not found exception. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
  /**
   * Instantiates a new Resource not found exception.
   *
   * @param message the message
   */
  public NotFoundException(String message) {
    super(message);
  }
}

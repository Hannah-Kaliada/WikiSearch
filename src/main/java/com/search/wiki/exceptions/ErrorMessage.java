package com.search.wiki.exceptions;

/** The type Error message. */
public class ErrorMessage {
  private int status;
  private String message;

  /**
   * Instantiates a new Error message.
   *
   * @param status the status
   * @param message the message
   */
  public ErrorMessage(int status, String message) {
    this.status = status;
    this.message = message;
  }

  /**
   * Gets status.
   *
   * @return the status
   */
  public int getStatus() {
    return status;
  }

  /**
   * Sets status.
   *
   * @param status the status
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }
}

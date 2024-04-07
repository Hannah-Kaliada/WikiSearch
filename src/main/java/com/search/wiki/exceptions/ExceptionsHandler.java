package com.search.wiki.exceptions;

import com.search.wiki.exceptions.customexceptions.DatabaseAccessException;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.InvalidDataException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.exceptions.customexceptions.RemoteServiceUnavailableException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/** The type Exceptions handler. */
@ControllerAdvice
public class ExceptionsHandler {

  /**
   * Handle http client error exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<ErrorMessage> handleHttpClientErrorException(HttpClientErrorException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  /**
   * Handle http server error exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(HttpServerErrorException.class)
  public ResponseEntity<ErrorMessage> handleHttpServerErrorException(HttpServerErrorException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  /**
   * Handle data access exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorMessage> handleDataAccessException(DataAccessException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  /**
   * Handle not found exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
  }

  /**
   * Handle illegal argument exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  /**
   * Handle null pointer exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  /**
   * Handle number format exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<ErrorMessage> handleNumberFormatException(NumberFormatException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
  }

  /**
   * Handle unsupported operation exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ErrorMessage> handleUnsupportedOperationException(
      UnsupportedOperationException ex) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(new ErrorMessage(HttpStatus.NOT_IMPLEMENTED.value(), ex.getMessage()));
  }

  /**
   * Handle illegal state exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  /**
   * Handle invalid data exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<ErrorMessage> handleInvalidDataException(InvalidDataException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  /**
   * Handle duplicate entry exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DuplicateEntryException.class)
  public ResponseEntity<ErrorMessage> handleDuplicateEntryException(DuplicateEntryException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorMessage(HttpStatus.CONFLICT.value(), ex.getMessage()));
  }

  /**
   * Handle remote service unavailable exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(RemoteServiceUnavailableException.class)
  public ResponseEntity<ErrorMessage> handleRemoteServiceUnavailableException(
      RemoteServiceUnavailableException ex) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(new ErrorMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage()));
  }

  /**
   * Handle database access exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DatabaseAccessException.class)
  public ResponseEntity<ErrorMessage> handleDatabaseAccessException(DatabaseAccessException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  /**
   * Handle method argument not valid exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    StringBuilder errorMessage = new StringBuilder();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError ->
                errorMessage
                    .append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), errorMessage.toString()));
  }

  /**
   * Handle generic exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String message = ex.getMessage() != null ? ex.getMessage() : "Unknown error occurred";
    return ResponseEntity.status(status).body(new ErrorMessage(status.value(), message));
  }
}

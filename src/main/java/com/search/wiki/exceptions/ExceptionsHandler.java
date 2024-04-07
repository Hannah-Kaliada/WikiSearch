package com.search.wiki.exceptions;

import com.search.wiki.exceptions.customexceptions.DatabaseAccessException;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.InvalidDataException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.exceptions.customexceptions.RemoteServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;
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

  private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP =
      new HashMap<>();

  static {
    EXCEPTION_STATUS_MAP.put(HttpClientErrorException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(HttpServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS_MAP.put(DataAccessException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS_MAP.put(NotFoundException.class, HttpStatus.NOT_FOUND);
    EXCEPTION_STATUS_MAP.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(NullPointerException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS_MAP.put(NumberFormatException.class, HttpStatus.UNPROCESSABLE_ENTITY);
    EXCEPTION_STATUS_MAP.put(UnsupportedOperationException.class, HttpStatus.NOT_IMPLEMENTED);
    EXCEPTION_STATUS_MAP.put(IllegalStateException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS_MAP.put(InvalidDataException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(DuplicateEntryException.class, HttpStatus.CONFLICT);
    EXCEPTION_STATUS_MAP.put(
        RemoteServiceUnavailableException.class, HttpStatus.SERVICE_UNAVAILABLE);
    EXCEPTION_STATUS_MAP.put(DatabaseAccessException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS_MAP.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> handleException(Exception ex) {
    HttpStatus status =
        EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    String message = ex.getMessage() != null ? ex.getMessage() : "Unknown error occurred";
    return ResponseEntity.status(status).body(new ErrorMessage(status.value(), message));
  }
}

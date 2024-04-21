package com.search.wiki.service.utils;

import com.search.wiki.util.RequestCounter;
import org.springframework.stereotype.Service;

/**
 * The type Request count service.
 */
@Service
public class RequestCountService {

  private RequestCounter requestCounter = new RequestCounter();

  /**
   * Increment request count.
   */
public void incrementRequestCount() {
    requestCounter.increment();
  }

  /**
   * Gets request count.
   *
   * @return the request count
   */
  public int getRequestCount() {
    return requestCounter.getCount();
  }
}

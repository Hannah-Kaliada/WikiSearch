package com.search.wiki.service.utils;

import com.search.wiki.util.RequestCounter;
import org.springframework.stereotype.Service;

@Service
public class RequestCountService {

  private RequestCounter requestCounter = new RequestCounter();

  public void incrementRequestCount() {
    requestCounter.increment();
  }

  public int getRequestCount() {
    return requestCounter.getCount();
  }
}

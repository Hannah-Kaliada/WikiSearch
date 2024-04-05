package com.search.wiki.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** The type Logging aspect. */
@Aspect
@Component
public class LoggingAspect {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

  /** Log service methods. */
  @Pointcut("com.search.wiki.aspects.PointcutDefinitions.serviceMethods()")
  public void logServiceMethods() {}

  /** Log repository methods. */
  @Pointcut("com.search.wiki.aspects.PointcutDefinitions.repositoryMethods()")
  public void logRepositoryMethods() {}

  /** Log controller methods. */
  @Pointcut("com.search.wiki.aspects.PointcutDefinitions.controllerMethods()")
  public void logControllerMethods() {}

  /**
   * Log service exception.
   *
   * @param joinPoint the join point
   * @param exception the exception
   */
  @AfterThrowing(pointcut = "logServiceMethods()", throwing = "exception")
  public void logServiceException(final JoinPoint joinPoint, final Throwable exception) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.error(
        "Exception thrown in service method '{}' with message: '{}'",
        methodName,
        exception.getMessage());
  }

  /**
   * Log repository exception.
   *
   * @param joinPoint the join point
   * @param exception the exception
   */
  @AfterThrowing(pointcut = "logRepositoryMethods()", throwing = "exception")
  public void logRepositoryException(final JoinPoint joinPoint, final Throwable exception) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.error(
        "Exception thrown in repository method '{}' with message: '{}'",
        methodName,
        exception.getMessage());
  }

  /**
   * Log controller exception.
   *
   * @param joinPoint the join point
   * @param exception the exception
   */
  @AfterThrowing(pointcut = "logControllerMethods()", throwing = "exception")
  public void logControllerException(final JoinPoint joinPoint, final Throwable exception) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.error(
        "Exception thrown in controller method '{}' with message: '{}'",
        methodName,
        exception.getMessage());
  }

  /**
   * Log service method call.
   *
   * @param joinPoint the join point
   */
  @Before("logServiceMethods()")
  public void logServiceMethodCall(final JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Service method '{}' called", methodName);
  }

  /**
   * Log repository method call.
   *
   * @param joinPoint the join point
   */
  @Before("logRepositoryMethods()")
  public void logRepositoryMethodCall(final JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Repository method '{}' called", methodName);
  }

  /**
   * Log controller method call.
   *
   * @param joinPoint the join point
   */
  @Before("logControllerMethods()")
  public void logControllerMethodCall(final JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Controller method '{}' called", methodName);
  }

  /**
   * Log execution time for service methods.
   *
   * @param joinPoint the join point
   * @return the object
   * @throws Throwable the throwable
   */
  @Around("logServiceMethods()")
  public Object logServiceExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    LOGGER.info("Service method '{}' executed in {}ms", joinPoint.getSignature(), executionTime);
    return proceed;
  }

  /**
   * Log execution time for repository methods.
   *
   * @param joinPoint the join point
   * @return the object
   * @throws Throwable the throwable
   */
  @Around("logRepositoryMethods()")
  public Object logRepositoryExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    LOGGER.info("Repository method '{}' executed in {}ms", joinPoint.getSignature(), executionTime);
    return proceed;
  }

  /**
   * Log execution time for controller methods.
   *
   * @param joinPoint the join point
   * @return the object
   * @throws Throwable the throwable
   */
  @Around("logControllerMethods()")
  public Object logControllerExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed();
    long executionTime = System.currentTimeMillis() - start;
    LOGGER.info("Controller method '{}' executed in {}ms", joinPoint.getSignature(), executionTime);
    return proceed;
  }

  /**
   * Log service method return.
   *
   * @param joinPoint the join point
   * @param result the result
   */
  @AfterReturning(pointcut = "logServiceMethods()", returning = "result")
  public void logServiceMethodReturn(final JoinPoint joinPoint, final Object result) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Service method '{}' returned '{}'", methodName, result);
  }

  /**
   * Log repository method return.
   *
   * @param joinPoint the join point
   * @param result the result
   */
  @AfterReturning(pointcut = "logRepositoryMethods()", returning = "result")
  public void logRepositoryMethodReturn(final JoinPoint joinPoint, final Object result) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Repository method '{}' returned '{}'", methodName, result);
  }

  /**
   * Log controller method return.
   *
   * @param joinPoint the join point
   * @param result the result
   */
  @AfterReturning(pointcut = "logControllerMethods()", returning = "result")
  public void logControllerMethodReturn(final JoinPoint joinPoint, final Object result) {
    String methodName = joinPoint.getSignature().getName();
    LOGGER.info("Controller method '{}' returned '{}'", methodName, result);
  }
}

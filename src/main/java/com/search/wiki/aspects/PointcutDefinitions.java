package com.search.wiki.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/** The type Pointcut definitions. */
@Aspect
public class PointcutDefinitions {
  /** Pointcut for service methods. */
  @Pointcut("execution(* com.search.wiki.service.*.*(..))")
  public void serviceMethods() {}

  /** Pointcut for repository methods. */
  @Pointcut("execution(* com.search.wiki.repository.*.*(..))")
  public void repositoryMethods() {}

  /** Pointcut for controller methods. */
  @Pointcut("execution(* com.search.wiki.controller.*.*(..))")
  public void controllerMethods() {}
}

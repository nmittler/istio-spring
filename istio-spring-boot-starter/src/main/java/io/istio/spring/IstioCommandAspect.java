package io.istio.spring;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class IstioCommandAspect {

  IstioCommandAspect() {
  }

  @Pointcut("@annotation(com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand)")
  public void command() {
  }

  @Around("command()")
  public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {

    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    HystrixCommand annotation = method.getAnnotation(HystrixCommand.class);
    IstioContext.getInstance().init(annotation);
    System.err.println("NM: service aspect call: " + annotation);
    System.err.println("NM: service aspect thread ID=" + Thread.currentThread().getId());
    System.err.println("NM: service aspect join point: " + joinPoint);

    try {
      return joinPoint.proceed();
    } finally {
      IstioContext.getInstance().clear();
    }
  }
}

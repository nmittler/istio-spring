/*******************************************************************************
 * Copyright (c) 2017 Istio Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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

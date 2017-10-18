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

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.command.MetaHolder;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesCommandDefault;
import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    // Extract the properties from the annotation.
    HystrixCommandProperties commandProps = getCommandProperties(joinPoint);

    if (commandProps.circuitBreakerEnabled().get()) {
      // Convert to circuit breaker properties.
      CircuitBreakerProperties circuitBreakerProps = circuitBreakerProps(commandProps);

      // Set the properties on the thread-local context.
      IstioContext.getInstance().init(circuitBreakerProps);
      System.err.println("NM: service aspect call: " + circuitBreakerProps);
      System.err.println("NM: service aspect thread ID=" + Thread.currentThread().getId());
      System.err.println("NM: service aspect join point: " + joinPoint);
    } else {
      IstioContext.getInstance().clear();
    }

    try {
      return joinPoint.proceed();
    } finally {
      IstioContext.getInstance().clear();
    }
  }

  private static HystrixCommandProperties getCommandProperties(ProceedingJoinPoint joinPoint) {
    MetaHolder metaHolder = new MetaHolderFactory.CommandMetaHolderFactory().create(joinPoint);
    HystrixCommandProperties.Setter setter = HystrixPropertiesManager.initializeCommandProperties(
        metaHolder.getCommandProperties());
    HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey(metaHolder.getCommandKey());
    return new HystrixPropertiesCommandDefault(commandKey, setter);
  }

  private static CircuitBreakerProperties circuitBreakerProps(
      HystrixCommandProperties commandProps) {
    CircuitBreakerProperties.Builder builder = CircuitBreakerProperties.newBuilder();
    // TODO(nmittler): Convert more properties?
    builder.sleepWindow(
        Duration.ofMillis(commandProps.circuitBreakerSleepWindowInMilliseconds().get()));
    return builder.build();
  }
}

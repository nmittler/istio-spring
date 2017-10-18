/**
 * Copyright (c) 2017 Istio Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright 2012 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Copyright 2012 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.istio.spring;

import static com.netflix.hystrix.contrib.javanica.utils.AopUtils.getMethodFromTarget;
import static com.netflix.hystrix.contrib.javanica.utils.EnvUtils.isCompileWeaving;

import com.google.common.base.Optional;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ExecutionType;
import com.netflix.hystrix.contrib.javanica.command.MetaHolder;
import com.netflix.hystrix.contrib.javanica.utils.AopUtils;
import com.netflix.hystrix.contrib.javanica.utils.FallbackMethod;
import com.netflix.hystrix.contrib.javanica.utils.MethodProvider;
import com.netflix.hystrix.contrib.javanica.utils.ajc.AjcUtils;
import com.netflix.hystrix.contrib.javanica.utils.ajc.AjcUtils.AdviceType;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Factory for Hystrix {@link MetaHolder} instances.
 */
abstract class MetaHolderFactory {

  public MetaHolder create(final ProceedingJoinPoint joinPoint) {
    Method method = getMethodFromTarget(joinPoint);
    Object obj = joinPoint.getTarget();
    Object[] args = joinPoint.getArgs();
    Object proxy = joinPoint.getThis();
    return create(proxy, method, obj, args, joinPoint);
  }

  public abstract MetaHolder create(Object proxy, Method method, Object obj, Object[] args,
      final ProceedingJoinPoint joinPoint);

  MetaHolder.Builder metaHolderBuilder(Object proxy, Method method, Object obj, Object[] args,
      final ProceedingJoinPoint joinPoint) {
    MetaHolder.Builder builder = MetaHolder.builder()
        .args(args).method(method).obj(obj).proxyObj(proxy)
        .joinPoint(joinPoint);

    setFallbackMethod(builder, obj.getClass(), method);
    builder = setDefaultProperties(builder, obj.getClass(), joinPoint);
    return builder;
  }

  static final class CommandMetaHolderFactory extends MetaHolderFactory {

    public MetaHolder create(final ProceedingJoinPoint joinPoint) {
      Method method = getMethodFromTarget(joinPoint);
      Object obj = joinPoint.getTarget();
      Object[] args = joinPoint.getArgs();
      Object proxy = joinPoint.getThis();
      return create(proxy, method, obj, args, joinPoint);
    }

    @Override
    public MetaHolder create(Object proxy, Method method, Object obj, Object[] args,
        final ProceedingJoinPoint joinPoint) {
      HystrixCommand hystrixCommand = method.getAnnotation(HystrixCommand.class);
      ExecutionType executionType = ExecutionType.getExecutionType(method.getReturnType());
      MetaHolder.Builder builder = metaHolderBuilder(proxy, method, obj, args, joinPoint);
      if (isCompileWeaving()) {
        builder.ajcMethod(getAjcMethodFromTarget(joinPoint));
      }
      return builder.defaultCommandKey(method.getName())
          .hystrixCommand(hystrixCommand)
          .observableExecutionMode(hystrixCommand.observableExecutionMode())
          .executionType(executionType)
          .observable(ExecutionType.OBSERVABLE == executionType)
          .build();
    }
  }

  private static Method getAjcMethodFromTarget(JoinPoint joinPoint) {
    return getAjcMethodAroundAdvice(joinPoint.getTarget().getClass(),
        (MethodSignature) joinPoint.getSignature());
  }

  private static Method getAjcMethodAroundAdvice(final Class<?> target, final String methodName,
      final Class<?>... pTypes) {
    return AjcUtils.getAjcMethod(target, methodName, AdviceType.Around, pTypes);
  }


  private static Method getAjcMethodAroundAdvice(Class<?> target, MethodSignature signature) {
    return getAjcMethodAroundAdvice(target, signature.getMethod().getName(),
        signature.getParameterTypes());
  }


  private static Method getAjcMethodAroundAdvice(Class<?> target, Method method) {
    return getAjcMethodAroundAdvice(target, method.getName(), method.getParameterTypes());
  }

  private static MetaHolder.Builder setDefaultProperties(MetaHolder.Builder builder,
      Class<?> declaringClass, final ProceedingJoinPoint joinPoint) {
    Optional<DefaultProperties> defaultPropertiesOpt = AopUtils
        .getAnnotation(joinPoint, DefaultProperties.class);
    builder.defaultGroupKey(declaringClass.getSimpleName());
    if (defaultPropertiesOpt.isPresent()) {
      DefaultProperties defaultProperties = defaultPropertiesOpt.get();
      builder.defaultProperties(defaultProperties);
      if (StringUtils.isNotBlank(defaultProperties.groupKey())) {
        builder.defaultGroupKey(defaultProperties.groupKey());
      }
      if (StringUtils.isNotBlank(defaultProperties.threadPoolKey())) {
        builder.defaultThreadPoolKey(defaultProperties.threadPoolKey());
      }
    }
    return builder;
  }

  private static MetaHolder.Builder setFallbackMethod(MetaHolder.Builder builder,
      Class<?> declaringClass, Method commandMethod) {
    FallbackMethod fallbackMethod = MethodProvider
        .getInstance().getFallbackMethod(declaringClass, commandMethod);
    if (fallbackMethod.isPresent()) {
      fallbackMethod.validateReturnType(commandMethod);
      builder
          .fallbackMethod(fallbackMethod.getMethod())
          .fallbackExecutionType(
              ExecutionType.getExecutionType(fallbackMethod.getMethod().getReturnType()));
    }
    return builder;
  }
}

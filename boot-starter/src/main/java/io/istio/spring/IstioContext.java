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

final class IstioContext {
  private static final ThreadLocal<IstioContext> currentCommand =
      new ThreadLocal<IstioContext>() {
        @Override protected IstioContext initialValue() {
          return new IstioContext();
        }
      };

  static IstioContext getInstance() {
    return currentCommand.get();
  }

  private CircuitBreakerProperties circuitBreakerProperties;

  void init(CircuitBreakerProperties circuitBreakerProperties) {
    this.circuitBreakerProperties = circuitBreakerProperties;
  }

  CircuitBreakerProperties getCircuitBreakerProperties() {
    return circuitBreakerProperties;
  }

  void clear() {
    this.circuitBreakerProperties = null;
  }
}

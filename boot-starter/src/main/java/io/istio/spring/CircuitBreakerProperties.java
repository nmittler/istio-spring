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

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Duration;
import javax.annotation.Nullable;

/**
 * The Istio circuit breaker configuration.
 */
public final class CircuitBreakerProperties {
  private static final Duration ONE_MS = Duration.ofMillis(1);
  private static final CircuitBreakerProperties DEFAULTS =
      newBuilder()
          .maxConnections(Integer.MAX_VALUE)
          .httpMaxPendingRequests(1024)
          .httpMaxRequests(1024)
          .sleepWindow(Duration.ofSeconds(30))
          .httpConsecutiveErrors(5)
          .httpDetectionInterval(Duration.ofSeconds(10))
          .httpMaxRequestsPerConnection(Integer.MAX_VALUE)
          .httpMaxEjectionPercent(10)
          .build();

  private final Integer maxConnections;
  private final Integer httpMaxPendingRequests;
  private final Integer httpMaxRequests;
  private final Duration sleepWindow;
  private final Integer httpConsecutiveErrors;
  private final Duration httpDetectionInterval;
  private final Integer httpMaxRequestsPerConnection;
  private final Integer httpMaxEjectionPercent;

  private CircuitBreakerProperties(Builder builder) {
    this.maxConnections = builder.maxConnections;
    this.httpMaxPendingRequests = builder.httpMaxPendingRequests;
    this.httpMaxRequests = builder.httpMaxRequests;
    this.sleepWindow = builder.sleepWindow;
    this.httpConsecutiveErrors = builder.httpConsecutiveErrors;
    this.httpDetectionInterval = builder.httpDetectionInterval;
    this.httpMaxRequestsPerConnection = builder.httpMaxRequestsPerConnection;
    this.httpMaxEjectionPercent = builder.httpMaxEjectionPercent;
  }

  @Nullable
  public Integer getMaxConnections() {
    return maxConnections;
  }

  @Nullable
  public Integer getHttpMaxPendingRequests() {
    return httpMaxPendingRequests;
  }

  @Nullable
  public Integer getHttpMaxRequests() {
    return httpMaxRequests;
  }

  @Nullable
  public Duration getSleepWindow() {
    return sleepWindow;
  }

  @Nullable
  public Integer getHttpConsecutiveErrors() {
    return httpConsecutiveErrors;
  }

  @Nullable
  public Duration getHttpDetectionInterval() {
    return httpDetectionInterval;
  }

  @Nullable
  public Integer getHttpMaxRequestsPerConnection() {
    return httpMaxRequestsPerConnection;
  }

  @Nullable
  public Integer getHttpMaxEjectionPercent() {
    return httpMaxEjectionPercent;
  }

  @Override
  public String toString() {
    return "CircuitBreakerProperties{"
        + "maxConnections=" + maxConnections + ", httpMaxPendingRequests=" + httpMaxPendingRequests
        + ", httpMaxRequests=" + httpMaxRequests + ", sleepWindow=" + sleepWindow
        + ", httpConsecutiveErrors=" + httpConsecutiveErrors + ", httpDetectionInterval="
        + httpDetectionInterval + ", httpMaxRequestsPerConnection=" + httpMaxRequestsPerConnection
        + ", httpMaxEjectionPercent=" + httpMaxEjectionPercent + '}';
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Integer maxConnections;
    private Integer httpMaxPendingRequests;
    private Integer httpMaxRequests;
    private Duration sleepWindow;
    private Integer httpConsecutiveErrors;
    private Duration httpDetectionInterval;
    private Integer httpMaxRequestsPerConnection;
    private Integer httpMaxEjectionPercent;

    public Builder maxConnections(int maxConnections) {
      this.maxConnections = maxConnections;
      return this;
    }

    public Builder httpMaxPendingRequests(int httpMaxPendingRequests) {
      this.httpMaxPendingRequests = httpMaxPendingRequests;
      return this;
    }

    public Builder httpMaxRequests(int httpMaxRequests) {
      this.httpMaxRequests = httpMaxRequests;
      return this;
    }

    public Builder sleepWindow(Duration sleepWindow) {
      if (sleepWindow.compareTo(ONE_MS) < 0) {
        throw new IllegalArgumentException("sleepWindow must be >= 1ms");
      }
      this.sleepWindow = checkNotNull(sleepWindow);

      return this;
    }

    public Builder httpConsecutiveErrors(int httpConsecutiveErrors) {
      this.httpConsecutiveErrors = httpConsecutiveErrors;
      return this;
    }

    public Builder httpDetectionInterval(Duration httpDetectionInterval) {
      if (httpDetectionInterval.compareTo(ONE_MS) < 0) {
        throw new IllegalArgumentException("httpDetectionInterval must be >= 1ms");
      }
      this.httpDetectionInterval = httpDetectionInterval;
      return this;
    }

    public Builder httpMaxRequestsPerConnection(int httpMaxRequestsPerConnection) {
      this.httpMaxRequestsPerConnection = httpMaxRequestsPerConnection;
      return this;
    }

    public Builder httpMaxEjectionPercent(int httpMaxEjectionPercent) {
      this.httpMaxEjectionPercent = httpMaxEjectionPercent;
      return this;
    }

    public CircuitBreakerProperties build() {
      return new CircuitBreakerProperties(this);
    }
  }
}

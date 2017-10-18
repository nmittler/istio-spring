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

/**
 * Headers supported by Envoy to control request behavior.
 */
public final class EnvoyHeaders {

  private EnvoyHeaders() {
  }

  public final String X_ENVOY_EXPECTED_RQ_TIMEOUT_MS = "x-envoy-expected-rq-timeout-ms";
  public final String X_ENVOY_MAX_RETRIES = "x-envoy-max-retries";
  public final String X_ENVOY_RETRY_ON = "x-envoy-retry-on";
  public final String X_ENVOY_RETRY_GRPC_ON = "x-envoy-retry-grpc-on";
  public final String X_ENVOY_UPSTREAM_ALT_STAT_NAME = "x-envoy-upstream-alt-stat-name";
  public final String X_ENVOY_UPSTREAM_CANARY = "x-envoy-upstream-canary";
  public final String X_ENVOY_UPSTREAM_RQ_TIMEOUT_ALT_RESPONSE =
      "x-envoy-upstream-rq-timeout-alt-response";
  public final String X_ENVOY_UPSTREAM_RQ_TIMEOUT_MS = "x-envoy-upstream-rq-timeout-ms";
  public final String X_ENVOY_UPSTREAM_RQ_PER_TRY_TIMEOUT_MS =
      "x-envoy-upstream-rq-per-try-timeout-ms";
  public final String X_ENVOY_UPSTREAM_SERVICE_TIME = "x-envoy-upstream-service-time";
  public final String X_ENVOY_ORIGINAL_PATH = "x-envoy-original-path";
  public final String X_ENVOY_IMMEDIATE_HEALTH_CHECK_FAIL = "x-envoy-immediate-health-check-fail";
  public final String X_ENVOY_OVERLOADED = "x-envoy-overloaded";
}

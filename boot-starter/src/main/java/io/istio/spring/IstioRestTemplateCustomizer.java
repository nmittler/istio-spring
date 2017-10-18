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

import java.io.IOException;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

public class IstioRestTemplateCustomizer implements RestTemplateCustomizer {

  @Override
  public void customize(RestTemplate restTemplate) {
    // Add an interceptor to identify all URIs that are being requested during a service method.
    restTemplate.getInterceptors().add(new Interceptor());
  }

  private static final class Interceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {

      System.err.println("NM: RestTemplate thread ID=" + Thread.currentThread().getId());
      CircuitBreakerProperties props = IstioContext.getInstance().getCircuitBreakerProperties();
      if (props != null) {
        // TODO(nmittler): Add Envoy headers (See https://envoyproxy.github.io/envoy/configuration/http_filters/router_filter.html#http-headers)
        System.err.println(
            "NM: Making request with config: " + props + " to endpoint: " + request.getURI());
      }
      return execution.execute(request, body);
    }
  }
}

package io.istio.spring;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
      HystrixCommand commandConfig = IstioContext.getInstance().getCommandConfig();
      if (commandConfig != null) {
        System.err.println("NM: Making request with config: " + commandConfig + " to endpoint: " + request.getURI());
      }
      return execution.execute(request, body);
    }
  }
}

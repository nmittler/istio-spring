package io.istio.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IstioAutoConfiguration {

  IstioAutoConfiguration() {
  }

  /**
   * If {@code spring-cloud-netflix-core} is on classpath, this will override the
   * {@code hystrixCommandAspect} in {@code HystrixCommandAspect}. The method name is important.
   */
  @Bean
  public IstioCommandAspect hystrixCommandAspect() {
    return new IstioCommandAspect();
  }

  @Bean
  public IstioRestTemplateCustomizer istioRestTemplateCustomizer() {
    return new IstioRestTemplateCustomizer();
  }
}

package io.istio.reviews;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "review")
public class ReviewProperties {

  private String starColor;

  public String getStarColor() {
    return starColor;
  }

  public void setStarColor(String starColor) {
    this.starColor = starColor;
  }
}

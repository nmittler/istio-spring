package io.istio.reviews.response;

import static com.google.common.base.Preconditions.checkNotNull;

public final class StarRating {
  private final int stars;
  private final String color;

  public StarRating(int stars, String color) {
    this.stars = stars;
    this.color = checkNotNull(color, "color");
  }

  public int getStars() {
    return stars;
  }

  public String getColor() {
    return color;
  }
}

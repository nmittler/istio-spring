package io.istio.spring.reviews.response;

import com.google.common.collect.ImmutableList;
import java.util.List;

public final class Reviews {
  private final int id;
  private final List<Review> reviews;

  public Reviews(int id, List<Review> reviews) {
    this.id = id;
    this.reviews = ImmutableList.copyOf(reviews);
  }

  public int getId() {
    return id;
  }

  public List<Review> getReviews() {
    return reviews;
  }
}

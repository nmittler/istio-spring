package io.istio.reviews.response;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Review {
  private final String reviewer;
  private final String text;
  private final StarRating starRating;

  public Review(String reviewer, String text, StarRating starRating) {
    this.reviewer = checkNotNull(reviewer, "reviewer");
    this.text = checkNotNull(text, "text");
    this.starRating = checkNotNull(starRating, "starRating");
  }

  public String getReviewer() {
    return reviewer;
  }

  public String getText() {
    return text;
  }

  public StarRating getStarRating() {
    return starRating;
  }
}

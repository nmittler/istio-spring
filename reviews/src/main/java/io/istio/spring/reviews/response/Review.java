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
package io.istio.spring.reviews.response;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Optional;

public final class Review {
  private final String reviewer;
  private final String text;

  @JsonInclude(Include.NON_EMPTY)
  private final Optional<StarRating> rating;

  public Review(String reviewer, String text, StarRating starRating) {
    this.reviewer = checkNotNull(reviewer, "reviewer");
    this.text = checkNotNull(text, "text");
    this.rating = Optional.ofNullable(starRating);
  }

  public String getReviewer() {
    return reviewer;
  }

  public String getText() {
    return text;
  }

  public Optional<StarRating> getRating() {
    return rating;
  }
}

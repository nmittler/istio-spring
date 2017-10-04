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

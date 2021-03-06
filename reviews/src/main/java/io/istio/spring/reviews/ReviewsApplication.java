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
package io.istio.spring.reviews;

import static io.istio.spring.reviews.Headers.X_B3_FLAGS;
import static io.istio.spring.reviews.Headers.X_B3_PARENTSPANID;
import static io.istio.spring.reviews.Headers.X_B3_SAMPLED;
import static io.istio.spring.reviews.Headers.X_B3_SPANID;
import static io.istio.spring.reviews.Headers.X_B3_TRACEID;
import static io.istio.spring.reviews.Headers.X_OT_SPAN_CONTEXT;
import static io.istio.spring.reviews.Headers.X_REQUEST_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.istio.spring.api.Review;
import io.istio.spring.api.ReviewsResponse;
import io.istio.spring.api.Stars;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// TODO(nmittler): Sort out how to register the Istio starter for this.
//@EnableCircuitBreaker
@RestController
@SpringBootApplication
public class ReviewsApplication {

  @Autowired
  private ReviewProperties props;

  @Autowired
  private RatingService ratingService;

  @RequestMapping(value = "/health", method = GET, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public Map<String, String> health() {
    return Collections.singletonMap("status", "Reviews is healthy");
  }

  @RequestMapping(value = "/reviews/{productId}", method = GET, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public ReviewsResponse bookReviewsById(@PathVariable("productId") int productId,
      @CookieValue("user") String user,
      @RequestHeader(value = X_REQUEST_ID, required = false) String xreq,
      @RequestHeader(value = X_B3_TRACEID, required = false) String xtraceid,
      @RequestHeader(value = X_B3_SPANID, required = false) String xspanid,
      @RequestHeader(value = X_B3_PARENTSPANID, required = false) String xparentspanid,
      @RequestHeader(value = X_B3_SAMPLED, required = false) String xsampled,
      @RequestHeader(value = X_B3_FLAGS, required = false) String xflags,
      @RequestHeader(value = X_OT_SPAN_CONTEXT, required = false) String xotspan)
      throws IOException {

    // Invoke the ratings service and get the JSON response.
    String json = ratingService.getRatings(
        productId, user, xreq, xtraceid, xspanid, xparentspanid, xsampled, xflags, xotspan);

    ObjectMapper mapper = new ObjectMapper();

    JsonNode response = mapper.readTree(json);

    JsonNode r1 = response.path("ratings").path("Reviewer1");
    Stars sr1 = r1.isMissingNode() ? null : newStars(r1.asInt());

    JsonNode r2 = response.path("ratings").path("Reviewer2");
    Stars sr2 = r2.isMissingNode() ? null : newStars(r2.asInt());

    return new ReviewsResponse(productId,
        Arrays.asList(new Review("Reviewer1",
            "Reviews v4 is simply amazing!", sr1),
            new Review("Reviewer2",
                "Not bad, but do we really need circuit breaking for this service?",
                sr2)));
  }

  private Stars newStars(int stars) {
    return new Stars(stars, props.getStarColor());
  }

  public static void main(String[] args) {
    SpringApplication.run(ReviewsApplication.class, args);
  }
}
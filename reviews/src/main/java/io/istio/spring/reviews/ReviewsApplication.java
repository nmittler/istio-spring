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
import io.istio.spring.reviews.response.Review;
import io.istio.spring.reviews.response.Reviews;
import io.istio.spring.reviews.response.StarRating;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  public Reviews bookReviewsById(@PathVariable("productId") int productId,
      @CookieValue("user") String user,
      @RequestHeader(value = X_REQUEST_ID, required = false) String xreq,
      @RequestHeader(value = X_B3_TRACEID, required = false) String xtraceid,
      @RequestHeader(value = X_B3_SPANID, required = false) String xspanid,
      @RequestHeader(value = X_B3_PARENTSPANID, required = false) String xparentspanid,
      @RequestHeader(value = X_B3_SAMPLED, required = false) String xsampled,
      @RequestHeader(value = X_B3_FLAGS, required = false) String xflags,
      @RequestHeader(value = X_OT_SPAN_CONTEXT, required = false) String xotspan)
      throws IOException {
    int starsReviewer1 = -1;
    int starsReviewer2 = -1;

    // Invoke the ratings service and get the JSON response.
    String json = ratingService.getRatings(
        productId, user, xreq, xtraceid, xspanid, xparentspanid, xsampled, xflags, xotspan);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode response = mapper.readTree(json);

    JsonNode r1 = response.path("ratings/Reviewer1");
    if (!r1.isMissingNode()) {
      starsReviewer1 = r1.asInt();
    }

    JsonNode r2 = response.path("ratings/Reviewer2");
    if (!r2.isMissingNode()) {
      starsReviewer2 = r2.asInt();
    }

    return new Reviews(productId,
        Arrays.asList(newReview("Reviewer1",
            "An extremely entertaining play by Shakespeare. "
                + "The slapstick humour is refreshing!", starsReviewer1),
            newReview("Reviewer2",
                "Absolutely fun and entertaining. "
                    + "The play lacks thematic depth when compared to other plays by Shakespeare.",
                starsReviewer2)));
  }

  private Review newReview(String reviewer, String text, int stars) {
    return new Review(reviewer, text, new StarRating(stars, props.getStarColor()));
  }

  public static void main(String[] args) {
    SpringApplication.run(ReviewsApplication.class, args);
  }
}
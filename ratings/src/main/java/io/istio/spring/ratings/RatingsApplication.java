package io.istio.spring.ratings;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import io.istio.spring.api.RatingsResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RatingsApplication {

  @RequestMapping(value = "/ratings", method = GET, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public RatingsResponse getRatings() throws Exception {
    throw new NotFoundException("{error: \"Circuit Opened!\"}");
  }

  public static void main(String[] args) {
    SpringApplication.run(RatingsApplication.class, args);
  }
}

package io.istio.reviews;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.net.URI;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {

  @Autowired
  private RatingProperties props;

  @Autowired
  private RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "reliable")
  public String getRatings(int productId, String user, String xreq,
      String xtraceid,
      String xspanid,
      String xparentspanid,
      String xsampled,
      String xflags,
      String xotspan) {
    if (!props.isEnabled()) {
      return "{}";
    }

    URI url = URI.create(props.getUrl() + "/" + productId);

    // Copy the incoming request headers to the outbound request.
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(APPLICATION_JSON));

    if (user != null) {
      headers.add(HttpHeaders.COOKIE, "user=" + user);
    }
    if (xreq != null) {
      headers.add(Headers.X_REQUEST_ID, xreq);
    }
    if (xtraceid != null) {
      headers.add(Headers.X_B3_TRACEID, xtraceid);
    }
    if (xspanid != null) {
      headers.add(Headers.X_B3_SPANID, xspanid);
    }
    if (xparentspanid != null) {
      headers.add(Headers.X_B3_PARENTSPANID, xparentspanid);
    }
    if (xsampled != null) {
      headers.add(Headers.X_B3_SAMPLED, xsampled);
    }
    if (xflags != null) {
      headers.add(Headers.X_B3_FLAGS, xflags);
    }
    if (xotspan != null) {
      headers.add(Headers.X_OT_SPAN_CONTEXT, xotspan);
    }

    RequestEntity<String> entity = new RequestEntity<>(headers, GET, url);
    ResponseEntity<String> response = restTemplate.exchange(entity, String.class);
    return response.getBody();
  }

  public String reliable() {
    return "{}";
  }
}

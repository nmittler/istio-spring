package io.istio.reviews;

final class Headers {

  private Headers() {}

  static final String X_REQUEST_ID = "x-request-id";
  static final String X_B3_TRACEID = "x-b3-traceid";
  static final String X_B3_SPANID = "x-b3-spanid";
  static final String X_B3_PARENTSPANID = "x-b3-parentspanid";
  static final String X_B3_SAMPLED = "x-b3-sampled";
  static final String X_B3_FLAGS = "x-b3-flags";
  static final String X_OT_SPAN_CONTEXT = "x-ot-span-context";
}

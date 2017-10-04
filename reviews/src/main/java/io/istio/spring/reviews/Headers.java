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

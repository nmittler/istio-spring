# Copyright 2017 Istio Authors
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

FROM openjdk:8-jdk

ARG serverPort=9080
ARG enableRatings=true
ARG starColor=blue

ENV SERVER_PORT=${serverPort}
ENV ENABLE_RATINGS=${enableRatings}
ENV STAR_COLOR=${starColor}

# Copy and build the app source
COPY . /usr/src/spring-istio
WORKDIR /usr/src/spring-istio
RUN ./gradlew clean build

# Run the app
EXPOSE ${SERVER_PORT}
CMD java -jar reviews/build/libs/istio-spring-reviews-1.0.0-SNAPSHOT.jar \
         --server.port=${SERVER_PORT} \
         --rating.service.enabled=${ENABLE_RATINGS} \
         --review.star-color=${STAR_COLOR}

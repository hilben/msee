#!/bin/bash
mvn clean install; \
cd grounding-webservice; \
mvn jetty:run; \
cd ..

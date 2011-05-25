#!/bin/bash
mvn clean install; \
cd invoker-webservice; \
mvn jetty:run; \
cd ..

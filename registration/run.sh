#!/bin/bash
mvn clean install; \
cd registration-webservice; \
mvn jetty:run; \
cd ..

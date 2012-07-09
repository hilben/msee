#!/bin/bash
mvn clean install; \
cd management-webservice; \
mvn jetty:run; \
cd ..

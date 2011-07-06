#!/bin/bash
mvn clean install; \
        cd discovery-webservice; \
        mvn jetty:run; \
        cd ..

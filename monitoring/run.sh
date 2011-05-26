#!/bin/bash
mvn clean package install; \
        cd monitoring-webservice; \
        mvn jetty:run; \
        cd ..

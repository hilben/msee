#!/bin/bash
mvn clean install; \
        cd invoker/invoker-webservice; \
        mvn jetty:run; \
        cd ../..

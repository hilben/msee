#!/bin/bash
cd invoker-project; \
        mvn clean package install; \
        cd ../invoker-webservice; \
        mvn jetty:run; \
        cd ..

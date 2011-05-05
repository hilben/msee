#!/bin/bash
cd invoker-project; \
        mvn clean package; \
        cd ../invoker-webservice; \
        mvn jetty:run; \
        cd ..

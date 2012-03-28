#!/bin/bash
mvn clean install; \
        cd invoker/invoker-webservice; \
        /home/koni/programs/maven/apache-maven-3.0.3/bin/mvn jetty:run; \
        cd ../..

#!/bin/bash
mvn clean install; \
        cd discovery-webservice; \
        /home/koni/programs/maven/apache-maven-3.0.3/bin/mvn jetty:run; \
        cd ..

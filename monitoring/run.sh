#!/bin/bash
/home/koni/programs/maven/apache-maven-3.0.3/bin/mvn clean package install; \
        cd monitoring-webservice; \
        /home/koni/programs/maven/apache-maven-3.0.3/bin/mvn jetty:run; \
        cd ..

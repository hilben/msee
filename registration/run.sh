#!/bin/bash
/home/koni/programs/maven/apache-maven-3.0.3/bin/mvn clean install; \
cd registration-webservice; \
/home/koni/programs/maven/apache-maven-3.0.3/bin/mvn jetty:run; \
cd ..

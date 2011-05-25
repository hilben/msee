#!/bin/bash
cd registration-project && \
        mvn clean install package && \
        cd .. && \
        cd registration-webservice && \
        mvn jetty:run


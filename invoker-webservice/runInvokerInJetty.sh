#!/bin/bash
port=8080
if [ "$1" != "" ]; then
  port=$1
else
  echo "\nYou can specify the port as first argument. $0 <port>, switching to default port $port"
fi
 
echo "\nUse http://localhost:$port/invoker-webservice/services/invoker?wsdl as entrypoint\n"
mvn -Dlog4j.configuration=file:./log4j.properties -Djetty.port=$port jetty:run

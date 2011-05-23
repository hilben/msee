#!/bin/bash
port=8080
debugPort=4000

if [ "$1" != "" ]; then
  port=$1
else
  echo "\nYou can specify the port as first argument. $0 <port> <debugPort>, switching to default port $port"
fi

if [ "$2" != "" ]; then
  debugPort=$2
else
  echo "\nYou can specify the debug port as second argument. $0 <port> <debugPort>, switching to default debug port $debugPort"
fi

echo "\nUse http://localhost:$port/invoker-webservice/services/invoker?wsdl as entrypoint\n"
echo "Use port $debugPort as debugging port\n"

export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$debugPort,server=y,suspend=y"
mvn -Dlog4j.configuration=file:./log4j.properties -Djetty.port=$port jetty:run

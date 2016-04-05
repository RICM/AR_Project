#!/bin/bash
java -Djava.security.policy="./server.policy" -Djava.security.manager -DLEVEL=ALL -jar MobilagentServer.jar Configurations/hello.server2.xml server2

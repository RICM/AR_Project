#!/bin/bash
java -Djava.security.policy="./server.policy" -Djava.security.manager -DLEVEL=ALL -jar MobilagentServer.jar Configurations/hello.client1.xml client


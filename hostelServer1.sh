#!/bin/bash
java -Djava.security.policy="/home/marwan/git/AR_Project/server.policy" -Djava.security.manager -DLEVEL=ALL -jar MobilagentServer.jar Configurations/hostel.server1.xml server1


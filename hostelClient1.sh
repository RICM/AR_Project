#!/bin/bash
java -Djava.security.policy="/home/marwan/git/AR_Project/server.policy" -Djava.security.manager -DLEVEL=ALL -jar MobilagentServer.jar Configurations/hostel.client1.xml client


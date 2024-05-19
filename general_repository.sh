#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_GENERAL_REPOSITORY.ua.pt"
CODEBASE="http://l040101-ws$MACHINE_REGISTRY.ua.pt/sd$LAB$GROUP/classes/"

echo "- General Repository will be deployed on $NODE"

echo "-- Transferring data to the General Repository node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/GeneralRepository"
sshpass -f password scp -r dist/GeneralRepository.zip "$NODE":~

echo "-- Decompressing data sent to the General Repository node"
sshpass -f password ssh "$NODE" "unzip -q ~/GeneralRepository.zip -d ~"

echo "-- Executing the General Repository program"
sshpass -f password ssh "$NODE" "fuser -k $PORT_GENERAL_REPOSITORY/tcp > /dev/null 2>&1"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/GeneralRepository ;
  java -Djava.rmi.server.codebase=$CODEBASE \
       -Djava.rmi.server.useCodebaseOnly=true \
       -Djava.security.policy=java.policy \
       server.main.ServerGeneralRepository \
       $PORT_GENERAL_REPOSITORY \
       l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

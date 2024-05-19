#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_PLAYGROUND.ua.pt"
CODEBASE="http://l040101-ws$MACHINE_REGISTRY.ua.pt/sd$LAB$GROUP/classes/"

echo "- Playground will be deployed on $NODE"

echo "-- Transferring data to the Playground node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/Playground"
sshpass -f password scp -r dist/Playground.zip "$NODE":~

echo "-- Decompressing data sent to the Playground node"
sshpass -f password ssh "$NODE" "unzip -q ~/Playground.zip -d ~"

echo "-- Executing the Playground program"
sshpass -f password ssh "$NODE" "fuser -k $PORT_PLAYGROUND/tcp > /dev/null 2>&1"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/Playground ;
  java -Djava.rmi.server.codebase=$CODEBASE \
       -Djava.rmi.server.useCodebaseOnly=true \
       -Djava.security.policy=java.policy \
       server.main.ServerPlayground \
       $PORT_PLAYGROUND \
       l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

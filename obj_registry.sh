#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_REGISTRY.ua.pt"
CODEBASE="http://l040101-ws$MACHINE_REGISTRY.ua.pt/sd$LAB$GROUP/classes/"

echo "- Remote Object Registry will be deployed on $NODE"

echo "-- Transferring data to the Remote Object Registry node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/ObjRegistry"
sshpass -f password scp -r dist/ObjRegistry.zip "$NODE":~

echo "-- Decompressing data sent to the Remote Object Registry node"
sshpass -f password ssh "$NODE" "unzip -q ~/ObjRegistry.zip -d ~"

echo "-- Executing the Remote Object Registry program"
sshpass -f password ssh "$NODE" "fuser -k $PORT_OBJ_REGISTRY/tcp > /dev/null 2>&1"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/ObjRegistry ;
  java -Djava.rmi.server.codebase=$CODEBASE \
       -Djava.rmi.server.useCodebaseOnly=true \
       -Djava.security.policy=java.policy \
       server.main.ServerRegisterRemoteObject \
       $PORT_OBJ_REGISTRY \
       l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_REGISTRY.ua.pt"
CODEBASE="http://l040101-ws$MACHINE_REGISTRY.ua.pt/sd$LAB$GROUP/classes/"

echo "- RMI Registry will be deployed on $NODE"

echo "-- Transferring data to the RMI Registry node"
sshpass -f password ssh "$NODE" "mkdir -p ~/Public/classes/interfaces"
sshpass -f password ssh "$NODE" "rm -rf ~/Public/classes/interfaces/*"
sshpass -f password scp -r dist/RMIRegistry.zip "$NODE":~

echo "-- Decompressing data sent to the RMI Registry node"
sshpass -f password ssh "$NODE" "unzip -uq ~/RMIRegistry.zip -d ~"
sshpass -f password ssh "$NODE" "cp -RT ~/dist/RMIRegistry/interfaces ~/Public/classes/interfaces"

echo "-- Executing the RMI Registry program"
sshpass -f password ssh "$NODE" "fuser -k $PORT_RMI_REGISTRY/tcp > /dev/null 2>&1"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/RMIRegistry ;
  rmiregistry -J-Djava.rmi.server.codebase=$CODEBASE \
              -J-Djava.rmi.server.useCodebaseOnly=true \
              $PORT_RMI_REGISTRY"

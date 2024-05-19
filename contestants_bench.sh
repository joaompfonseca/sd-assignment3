#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_CONTESTANTS_BENCH.ua.pt"
CODEBASE="http://l040101-ws$MACHINE_REGISTRY.ua.pt/sd$LAB$GROUP/classes/"

echo "- Contestants Bench will be deployed on $NODE"

echo "-- Transferring data to the Contestants Bench node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/ContestantsBench"
sshpass -f password scp -r dist/ContestantsBench.zip "$NODE":~

echo "-- Decompressing data sent to the Contestants Bench node"
sshpass -f password ssh "$NODE" "unzip -q ~/ContestantsBench.zip -d ~"

echo "-- Executing the Contestants Bench program"
sshpass -f password ssh "$NODE" "fuser -k $PORT_CONTESTANTS_BENCH/tcp > /dev/null 2>&1"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/ContestantsBench ;
  java -Djava.rmi.server.codebase=$CODEBASE \
       -Djava.rmi.server.useCodebaseOnly=true \
       -Djava.security.policy=java.policy \
       server.main.ServerContestantsBench \
       $PORT_CONTESTANTS_BENCH \
       l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

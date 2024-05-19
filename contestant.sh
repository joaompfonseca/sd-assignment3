#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_CONTESTANT.ua.pt"

echo "- Contestant will be deployed on $NODE"

echo "-- Transferring data to the Contestant node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/Contestant"
sshpass -f password scp -r dist/Contestant.zip "$NODE":~

echo "-- Decompressing data sent to the Contestant node"
sshpass -f password ssh "$NODE" "unzip -q ~/Contestant.zip -d ~"

echo "-- Executing the Contestant program"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/Contestant ;
  java client.main.ContestantClient \
  l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

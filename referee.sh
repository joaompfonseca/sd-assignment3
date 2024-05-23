#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_REFEREE.ua.pt"

echo "- Referee will be deployed on $NODE"

echo "-- Transferring data to the Referee node"
sshpass -f password scp -r dist/Referee.zip "$NODE":~

echo "-- Decompressing data sent to the Referee node"
sshpass -f password ssh "$NODE" "unzip -uq ~/Referee.zip -d ~"

echo "-- Executing the Referee program"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/Referee ;
  java client.main.RefereeClient \
  l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

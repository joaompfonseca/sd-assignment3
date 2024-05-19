#!/bin/bash
source load_env.sh

NODE="sd$LAB$GROUP@l040101-ws$MACHINE_COACH.ua.pt"

echo "- Coach will be deployed on $NODE"

echo "-- Transferring data to the Coach node"
sshpass -f password ssh "$NODE" "rm -rf ~/dist/Coach"
sshpass -f password scp -r dist/Coach.zip "$NODE":~

echo "-- Decompressing data sent to the Coach node"
sshpass -f password ssh "$NODE" "unzip -q ~/Coach.zip -d ~"

echo "-- Executing the Coach program"
sshpass -f password ssh "$NODE" \
  "cd ~/dist/Coach ;
  java client.main.CoachClient \
  l040101-ws$MACHINE_REGISTRY.ua.pt $PORT_RMI_REGISTRY"

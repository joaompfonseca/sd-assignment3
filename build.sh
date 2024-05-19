#!/bin/bash

echo "- Compiling the source code"
javac -source 1.8 -target 1.8 -Xlint:-options -Xlint:-removal -d bin */*.java */*/*.java

echo "- Distributing intermediate code to the different execution environments"
rm -rf dist
mkdir -p dist

echo "-- Coach"
mkdir -p dist/Coach/client/entities \
         dist/Coach/client/main \
         dist/Coach/interfaces/contestantsbench \
         dist/Coach/interfaces/playground \
         dist/Coach/interfaces/refereesite \
         dist/Coach/configuration
cp bin/client/entities/TCoach.class \
   dist/Coach/client/entities
cp bin/client/main/CoachClient.class \
   dist/Coach/client/main
cp bin/interfaces/contestantsbench/*.class \
   dist/Coach/interfaces/contestantsbench
cp bin/interfaces/playground/*.class \
   dist/Coach/interfaces/playground
cp bin/interfaces/refereesite/*.class \
   dist/Coach/interfaces/refereesite
cp bin/configuration/Config.class \
   dist/Coach/configuration

echo "-- Contestant"
mkdir -p dist/Contestant/client/entities \
         dist/Contestant/client/main \
         dist/Contestant/interfaces/contestantsbench \
         dist/Contestant/interfaces/playground \
         dist/Contestant/configuration
cp bin/client/entities/TContestant.class \
   dist/Contestant/client/entities
cp bin/client/main/ContestantClient.class \
   dist/Contestant/client/main
cp bin/interfaces/contestantsbench/*.class \
   dist/Contestant/interfaces/contestantsbench
cp bin/interfaces/playground/*.class \
   dist/Contestant/interfaces/playground
cp bin/configuration/Config.class \
   dist/Contestant/configuration

echo "-- Referee"
mkdir -p dist/Referee/client/entities \
         dist/Referee/client/main \
         dist/Referee/interfaces/playground \
         dist/Referee/interfaces/refereesite \
         dist/Referee/configuration
cp bin/client/entities/TReferee.class \
   dist/Referee/client/entities
cp bin/client/main/RefereeClient.class \
   dist/Referee/client/main
cp bin/interfaces/playground/*.class \
   dist/Referee/interfaces/playground
cp bin/interfaces/refereesite/*.class \
   dist/Referee/interfaces/refereesite
cp bin/configuration/Config.class \
   dist/Referee/configuration

echo "-- Contestants Bench"
mkdir -p dist/ContestantsBench/interfaces/contestantsbench \
         dist/ContestantsBench/interfaces/generalrepository \
         dist/ContestantsBench/configuration \
         dist/ContestantsBench/server/main \
         dist/ContestantsBench/server/objects
cp bin/interfaces/contestantsbench/*.class \
   dist/ContestantsBench/interfaces/contestantsbench
cp bin/interfaces/generalrepository/*.class \
   dist/ContestantsBench/interfaces/generalrepository
cp bin/interfaces/Register.class \
   dist/ContestantsBench/interfaces
cp bin/configuration/Config.class \
   dist/Referee/configuration
cp bin/server/main/ServerContestantsBench.class \
   dist/ContestantsBench/server/main
cp bin/server/objects/ContestantsBench*.class \
   bin/server/objects/RegisterRemoteObject.class \
   dist/ContestantsBench/server/objects
cp java.policy \
   dist/ContestantsBench

echo "-- Playground"
mkdir -p dist/Playground/interfaces/generalrepository \
         dist/Playground/interfaces/playground \
         dist/Playground/configuration \
         dist/Playground/server/main \
         dist/Playground/server/objects
cp bin/interfaces/generalrepository/*.class \
   dist/Playground/interfaces/generalrepository
cp bin/interfaces/playground/*.class \
   dist/Playground/interfaces/playground
cp bin/interfaces/Register.class \
   dist/Playground/interfaces
cp bin/configuration/Config.class \
   dist/Referee/configuration
cp bin/server/main/ServerPlayground.class \
   dist/Playground/server/main
cp bin/server/objects/Playground*.class \
   bin/server/objects/RegisterRemoteObject.class \
   dist/Playground/server/objects
cp java.policy \
   dist/Playground

echo "-- Referee Site"
mkdir -p dist/RefereeSite/interfaces/generalrepository \
         dist/RefereeSite/interfaces/refereesite \
         dist/RefereeSite/configuration \
         dist/RefereeSite/server/main \
         dist/RefereeSite/server/objects
cp bin/interfaces/generalrepository/*.class \
   dist/RefereeSite/interfaces/generalrepository
cp bin/interfaces/refereesite/*.class \
   dist/RefereeSite/interfaces/refereesite
cp bin/interfaces/Register.class \
   dist/RefereeSite/interfaces
cp bin/configuration/Config.class \
   dist/Referee/configuration
cp bin/server/main/ServerRefereeSite.class \
   dist/RefereeSite/server/main
cp bin/server/objects/RefereeSite*.class \
   bin/server/objects/RegisterRemoteObject.class \
   dist/RefereeSite/server/objects
cp java.policy \
   dist/RefereeSite

echo "-- General Repository"
mkdir -p dist/GeneralRepository/interfaces/generalrepository \
         dist/GeneralRepository/configuration \
         dist/GeneralRepository/server/main \
         dist/GeneralRepository/server/objects
cp bin/interfaces/generalrepository/*.class \
   dist/GeneralRepository/interfaces/generalrepository
cp bin/interfaces/Register.class \
   dist/GeneralRepository/interfaces
cp bin/configuration/Config.class \
   dist/Referee/configuration
cp bin/server/main/ServerGeneralRepository.class \
   dist/GeneralRepository/server/main
cp bin/server/objects/EGeneralRepository*.class \
   bin/server/objects/GeneralRepository*.class \
   bin/server/objects/RegisterRemoteObject.class \
   dist/GeneralRepository/server/objects
cp java.policy \
   dist/GeneralRepository

echo "-- Remote Object Registry"
mkdir -p dist/ObjRegistry/interfaces \
         dist/ObjRegistry/server/main \
         dist/ObjRegistry/server/objects
cp bin/interfaces/Register.class \
   dist/ObjRegistry/interfaces
cp bin/server/main/ServerRegisterRemoteObject.class \
   dist/ObjRegistry/server/main
cp bin/server/objects/RegisterRemoteObject.class \
   dist/ObjRegistry/server/objects
cp java.policy \
   dist/ObjRegistry

echo "-- RMI Registry"
mkdir -p dist/RMIRegistry/interfaces
cp -RT bin/interfaces \
   dist/RMIRegistry/interfaces

echo "- Compressing execution environments"

echo "-- Coach"
zip -rq dist/Coach.zip dist/Coach

echo "-- Contestant"
zip -rq dist/Contestant.zip dist/Contestant

echo "-- Referee"
zip -rq dist/Referee.zip dist/Referee

echo "-- Contestants Bench"
zip -rq dist/ContestantsBench.zip dist/ContestantsBench

echo "-- Playground"
zip -rq dist/Playground.zip dist/Playground

echo "-- Referee Site"
zip -rq dist/RefereeSite.zip dist/RefereeSite

echo "-- General Repository"
zip -rq dist/GeneralRepository.zip dist/GeneralRepository

echo "-- Remote Object Registry"
zip -rq dist/ObjRegistry.zip dist/ObjRegistry

echo "-- RMI Registry"
zip -rq dist/RMIRegistry.zip dist/RMIRegistry
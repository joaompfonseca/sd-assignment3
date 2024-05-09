echo "Compiling source code."
javac interfaces/*.java registry/*.java serverSide/*.java clientSide/*.java
echo "Distributing intermediate code to the different execution environments."
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp interfaces/Compute.class interfaces/Task.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
mkdir -p /home/diogopaiva21/Public/classes
mkdir -p /home/diogopaiva21/Public/classes/interfaces
mkdir -p /home/diogopaiva21/Public/classes/clientSide
cp interfaces/*.class /home/diogopaiva21/Public/classes/interfaces/
cp clientSide/Pi.class /home/diogopaiva21/Public/classes/clientSide/
echo "Compressing execution environments."
rm -f dir_registry.zip dir_serverSide.zip dir_clientSide.zip
zip -rq dir_registry.zip dir_registry
zip -rq dir_serverSide.zip dir_serverSide
zip -rq dir_clientSide.zip dir_clientSide
echo "Deploying and decompressing execution environments."
cp set_rmiregistry_alt.sh /home/diogopaiva21
cp set_rmiregistry.sh /home/diogopaiva21
mkdir -p /home/diogopaiva21/test/BackEngine
rm -rf /home/diogopaiva21/test/BackEngine/*
cp dir_registry.zip dir_serverSide.zip dir_clientSide.zip /home/diogopaiva21/test/BackEngine
cd /home/diogopaiva21/test/BackEngine
unzip -q dir_registry.zip
unzip -q dir_serverSide.zip
unzip -q dir_clientSide.zip

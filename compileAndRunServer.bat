set "BaseProjectPath=%UserProfile%\Socket-P2P-Network"
cd %BaseProjectPath%\src
javac gui/*.java
javac entity/*.java
javac socket/client/*.java
javac socket/server/*.java
java socket.server.Server
cd ..
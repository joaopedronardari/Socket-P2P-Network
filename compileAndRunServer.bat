set "BaseProjectPath=%UserProfile%\Socket-P2P-Network"
cd %BaseProjectPath%\src
javac gui/*.java
javac entity/*.java
javac socket/*.java
java socket.Server
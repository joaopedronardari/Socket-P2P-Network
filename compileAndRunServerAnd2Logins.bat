set "BaseProjectPath=%UserProfile%\Socket-P2P-Network"
cd %BaseProjectPath%\src
javac gui/*.java
javac entity/*.java
javac socket/client/*.java
javac socket/server/*.java
@start java gui.WindowLogin 
@start java gui.WindowLogin
java socket.server.Server 
cd ..
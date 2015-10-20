set "BaseProjectPath=%UserProfile%\Socket-P2P-Network"
cd %BaseProjectPath%\src
javac gui/*.java
javac entity/*.java
javac socket/*.java
@start java -Dhttp.proxyHost=proxy-chain.intel.com -Dhttp.proxyPort=911 -Dhttps.proxyHost=proxy-chain.intel.com -Dhttps.proxyPort=911 gui.WindowLogin 
@start java -Dhttp.proxyHost=proxy-chain.intel.com -Dhttp.proxyPort=911 -Dhttps.proxyHost=proxy-chain.intel.com -Dhttps.proxyPort=911 gui.WindowLogin
java -Dhttp.proxyHost=proxy-chain.intel.com -Dhttp.proxyPort=911 -Dhttps.proxyHost=proxy-chain.intel.com -Dhttps.proxyPort=911 socket.Server 
cd ..
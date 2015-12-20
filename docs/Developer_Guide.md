# Developer Guide
This developer guide is maintained by Shuoling. If you have any question, please contact me via [e-mail](mr.dengshuoling@gmail.com).

## Introdution
**InterONet** is a distributed system which contains the one **master** node called Global Domain Manager(`gdm`) and serveral **slave** node called Local Domain Manager(`ldm`).

The `gdm` is the master node which recieve the user request, allocate resources and call `ldm`. `gdm` is the independent of complicated type of devices in interonet. If you open the debug mode in the `gdm`, it will run alone without the `ldm`. `gdm` is mainly maintained by [Shuoling](mr.dengshuoling@gmail.com).

The `ldm` is the slave node which dependent on serveral device(`virtual-machine-manager`, `topology-transformer`, `power-system`). `ldm` is hard to install because it will interact with plenty of deivices. 


##  Installation
First, Clone the github repo and make a jar package.
```
$ git clone https://github.com/samueldeng/interonet
$ cd interonet/
$ mvn package -Dmaven.test.skip=true
```
These will produce two jar file.
```
$ ls  gdm/target/gdm-1.0-SNAPSHOT-jar-with-dependencies.jar
$ ls  ldm/target/gdm-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### GDM
First, Export Environment Virable.
```
$ export INTERONET_HOME=$PWD
$ echo $INTERONET
/home/samuel/interonet
```
> **NOTE:** Make sure `$INTERONET` is the repo base directory.

Then, Modify the GDM Configuration file `gdm/conf/conf.json`. 
```json
{
  "LDMConnectionURL":"http://localhost:8081/",
  "LDMConnectionReadTimeoutMillis": "1000000",
  "BootgenUtilWorkingDir": "/home/lab400/exp_image/",
  "TTPortMapDBTTPortMapDB":"/home/lab400/interonet/gdm/db/TTPortMap.json",
  "userDB":"/home/lab400/interonet/gdm/db/userDB.json",
  "SwitchesNumber":"3",
  "VMsNumber":"8"
}
```
> **NOTE:** Make sure the location of `TTPortMapDBTTPortMapDB` and `userDB` is correct.


Finally, Run the jar.
```bash
$ java -jar gdm/target/gdm-1.0-SNAPSHOT-jar-with-dependencies.jar
```
it will run successfully if the log is like the following.
```
 INFO [main] (org.interonet.gdm.Main:Main.java:13) - Starting InterONet GDM System
 INFO [main] (Main.java:13) - Starting InterONet GDM System
 INFO [main] (org.interonet.gdm.ConfigurationCenter.ConfigurationCenter:ConfigurationCenter.java:29) - reading conf file from/Users/samuel/interonet/gdm/conf/conf.json
 INFO [main] (ConfigurationCenter.java:29) - reading conf file from/Users/samuel/interonet/gdm/conf/conf.json
 INFO [main] (org.interonet.gdm.ConfigurationCenter.ConfigurationCenter:ConfigurationCenter.java:43) - reading TTProtMap file from/Users/samuel/github/interonet/gdm/db/TTPortMap.json
 INFO [main] (ConfigurationCenter.java:43) - reading TTProtMap file from/Users/samuel/github/interonet/gdm/db/TTPortMap.json
 INFO [main] (org.interonet.gdm.AuthenticationCenter.UserDBManager:UserDBManager.java:27) - reading userDB from/Users/samuel/github/interonet/gdm/db/userDB.json
 INFO [main] (UserDBManager.java:27) - reading userDB from/Users/samuel/github/interonet/gdm/db/userDB.json
 INFO [main] (org.interonet.gdm.Main:Main.java:17) - Starting InterONet GDM RPC Server
 INFO [main] (Main.java:17) - Starting InterONet GDM RPC Server
 ```

### LDM
> **TBA**

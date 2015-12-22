# User Guide
This user guide is maintained by Shuoling. If you have any question, please contact me via [e-mail](mr.dengshuoling@gmail.com)

## VPN Configuration
To use the InterONet : Full-Stack-Programmable SDN Testbed, the first step is connect to our VPN by PPTP.
### For Win7
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_1.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_2.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_3.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_4.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_5.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_6.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_7.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_8.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_9.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_10.png)
### Mac OS X
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_1.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_2.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_3.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_4.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_6.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_7.png)

## a Tour of Stat Slice
Slice is a collection of SDN switches, virtual machines and the links. Simply a slice is much more like a topology which contains switches, hosts and links.

### Submit a Slice Order on Web
1.  Open your browser, and open http://web.interonet.org/.
2.  Click the right up corner "login" link to access the your dashboard.
![](http://7xpcbm.com1.z0.glb.clouddn.com/home_page.png)
3.  Click the "Create Slice" on the left bar.
  1. Choose the number of virtual machine and SDN switches you wanted to order.
  2. Choose the start time and the end time you want to  use your slice. note that your have.
  3.  Choose every switch's configuration. In the v1.0, it's unavailable.
  4.  Choose the controller IP address and port number, we will made a start script in the switch in advance.
  5.  Create your topology through click the circle around the vm or switch.
  4.  If the order is available, you will get a slice of your wanted time.
4. Click "Order" Button to submit your request.
![](http://7xpcbm.com1.z0.glb.clouddn.com/order_page.png)
5. Check the waiting slice.
![](http://7xpcbm.com1.z0.glb.clouddn.com/waiting_slice_page.png)

### Login switches and virtual machines in Slice
After the order's status turn into running. you can using your own slice.
There are two types of devices in your slices including switches and virtual machines. The method to login every type of device is different. The switch is so **dummy** that it only support the **telnet** login. vritual machines can support **vnc** login.

### How to telnet into switch?
1. click "check" button to show the topology, and select which switches you want to login.
2. move the mouse to the switches on **SwitchMap** to show the connection tips.
3. Open the telnet software(eg. putty) and input the host name.
4. Connect to it.
The following imgae show the process to login into a switch.
![](http://7xpcbm.com1.z0.glb.clouddn.com/switch_login.png)

### How to vnc into vm?
1. click "check" button to show the topology, and select which host you want to login.
2. move the mouse to the switches on **VMMap** to show the connection tips.
3. Open the vnc software(eg. vnc viewer). Then input the host name and port number.
4. Connect to it.
5. Input the username and password we provide, and login into virtual machines.
The following imgae show the process to login into a host.
![](http://7xpcbm.com1.z0.glb.clouddn.com/host_login.png)

### Some Tips about dummy switches
> **NOTE:** the default startup/configuration script is in `/mnt/init.sh`.
change it when your have this switch, and reboot this switches to enable your configuration.

> **WARN:** **DONOT CHNAGE** `switch_ip`, `switch_netmask`, `gateway_ip`. If you do that, this switch will lose the connection with you.

```
#!/bin/sh
###
controller_ip=10.0.0.1
controller_port=6633
local_ip=127.0.0.1
local_port=6632

ofs_dir="/root/ofs-sw"
interfaces="eth1,eth2,eth3,eth4"
ofdatapath_options="--no-slicing"
ofprotocol_options="--inactivity-probe=90"
###
```
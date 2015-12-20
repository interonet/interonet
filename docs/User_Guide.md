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
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_5.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_6.png)
![](http://7xpcbm.com1.z0.glb.clouddn.com/vpn_setting_osx_7.png)

## Order Slice
Slice is a collection of SDN switches, virtual machines and the links. Simply a slice is much more like a topology which contains switches, hosts and links.

### Submit a Slice Order on Web
1.  Open your browser, and open http://10.0.0.1/.
2.  Click the right up corner "login" link to access the your dashboard.
3.  Click the "Create Slice" on the left bar.
  1. Choose the number of virtual machine and SDN switches you wanted to order.
  2. Choose the start time and the end time you want to  use your slice. note that your have.
  3.  Choose every switch's configuration. In the v1.0, it's unavailable.
  4.  Choose the controller IP address and port number, we will made a start script in the switch in advance.
  5.  Create your topology through click the circle around the vm or switch.
  4.  If the order is available, you will get a slice of your wanted time.
4. Click "Order" Button to submit your request.
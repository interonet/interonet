# Uboot Configuration Guide
This Guide is used to configurate the ONetSwitch in InterONet.

Every Switch must be configurate the PXE accoring to this guide.

Maintainer:  [Shuoling](mr.dengshuoling@gmail.com).
## Go Into U-Boot
1. sudo minicom -s
2. select serial port setup.
3. serial Device: /dev/ttyUSB0, Bps:115200 8N1, Hardware/Software Flow Control: No.
4. press Enter after power the onetswitch.
5. if the terminal show 'zynq-uboot>', then you can go next.

## U-Boot Command Line
The following command is what we use in this guide. There is no details ahout the concept of U-Boot. You can refer [THIS](http://www.denx.de/wiki/view/DULG/UBootEnvVariables) page to obtain more information. 
```
setenv: set environment variables.
printenv: print environment variables.
saveenv: save environment variables to SPI Flash.
```
We assume that the switch ID in interonet is 0. type the following command to set the boot environment into u-boot.
```
setenv bootargs 'console=ttyPS0,115200 ip=10.0.0.3:10.0.0.1:10.0.0.1:255.0.0.0::eth0:off root=/dev/nfs devtmpfs.mount=0 nfsroot=10.0.0.1:/export/0 rw earlyprintk'
```
Note that the 10.0.0.1 is the interonet LDM server. And /export/0 is the NFS directory in LDM Server.

Note that 10.0.0.3 is the switch0's ip address. In the early version of InterONet, we use a magic number 3 to do the mapping.
```
setenv ethaddr 00:0a:35:00:00:03
setenv ipaddr 10.0.0.3/24
```
Note that the ip and mac address are as above.
```
setenv interonetboot tftpboot 0x3000000 10.0.0.1:0/uImage\;tftpboot 0x2A00000 10.0.0.1:0/devicetree.dtb\;bootm 0x3000000 - 0x2A00000
setenv bootcmd $interonetboot
setenv bootdelay 1
```
These commands make this switch to download the uImage(Kernel) and devicetree from TFTP server.
```
saveenv
```
save it to the SPI Flash.

# Router System Configuration
## Interface Configuration
We have modified the file `/etc/network/interfaces`.
```
auto lo
iface lo inet loopback
auto eth0
iface eth0 inet static
address 202.117.15.222
gateway 202.117.15.1

auto eth1
iface eth1 inet static
address 10.0.0.1
netmask 255.0.0.0
```

## DNS Configuration 
The configuration files in our system are stored in `/etc/resolvconf/resolv.conf.d/base`
* We add the follow lines and then save it.
```
nameserver 202.117.0.20
nameserver 202.117.0.21
```
* Execute the following instruction
```
resolvconf -u
```
* Then you will find the file `/etc/resolv.conf` and add 2 lines in bottom.
```
nameserver 202.117.0.20
nameserver 202.117.0.21
```

## Open Forwarding
The configuration file in our system are stored in `/etc/sysctl.conf`.
* We add the following in order to open IPv4 forwarding:
```
net.ipv4.ip_forward=1
```
* Execute the following instruction(or Restart)：
```
sudo sysctl -p
```
 
## iptables SNAT Configuration
```
sudo iptables -t nat -A POSTROUTING -s 10.0.0.0/8 -o eth0 -j MASQUERADE
```
 
# PPTP System Configuration
 
## Install pptp
```
 sudo apt-get install pptp
```
 
## Configure IP address
 Edit `/etc/pptp.conf`
 ```
localip 10.255.255.1
remoteip 10.255.255.2-10.255.255.254
```

## Configure DNS
Edit `/etc/ppp/pptpd-option`
```
ms-dns 202.117.0.20
ms-dns 202.117.0.21
```

## Configue User's Information
Edit `/etc/ppp/chap-secrets`
```
# client        server  secret                  IP addresses
user1           pptpd   user1                   *
```

## Restart PPTP Service
```
sudo /etc/init.d/pptpd restart
```

## Configure the IPtable forwarding in order to make the PPTP client can connect the outer net
```
sudo iptables -t nat -A POSTROUTING -s 10.255.255.0/24 -o eth0 -j MASQUERADE
```
> **MISTAKE:** The following instruction is a common mistake, it would make all traffic be disguised as 202.117.15.119. So，the host can ping all ip address。**
> 
> ```
> sudo iptables -t nat -A PREROUTING -d 10.0.0.0/8 -j DNAT --to 202.117.15.119
> ```
> **NOTE:** If you have finish the above configuration, you will find you can not visit most web pages. It is because the traffic is limited. Enter the following instructions to resolve the problem.
```
sudo iptables -t filter -I FORWARD -p tcp --syn -i ppp+ -j TCPMSS --set-mss 1356
sudo iptables -A FORWARD -p tcp --syn -s 10.255.255.0/24 -j TCPMSS --set-mss 1356
```
# DNS System Configuration
In VPN Router，it contains a DNS SOA Server to forward and manage the `interonet.org` domain。

We use the bin9 to set up our dns name server as follow.
```
ii  bind9                               1:9.9.5.dfsg-3ubuntu0.6          amd64        Internet Domain Name Server
ii  bind9-doc                           1:9.9.5.dfsg-3ubuntu0.6          all          Documentation for BIND
ii  bind9-host                          1:9.9.5.dfsg-3ubuntu0.6          amd64        Version of 'host' bundled with BIND 9.X
ii  bind9utils                          1:9.9.5.dfsg-3ubuntu0.6          amd64        Utilities for BIND
ii  libbind9-90                         1:9.9.5.dfsg-3ubuntu0.6          amd64        BIND9 Shared Library used by BIND
```

## Configuration File
The configuration files in our system are stored in
```
/etc/bind/
```
We have modified these files.
```
-rw-r--r--  1 root bind  726 Dec 21 01:21 db.10
-rw-r--r--  1 root bind  492 Dec 21 00:43 db.interonet.org
-rw-r--r--  1 root bind 1057 Dec 21 01:19 named.conf.options
```
`named.conf.options`
* we add ACL `goodclients` to prevent the DNS Amp Attack.
```
acl goodclients {
    10.0.0.0/8;
    localhost;
    localnets;
};
options {
###
    recursion yes;
    allow-query { goodclients; };
###
};
 
```
* Then, we add forwarders to speed up recursive query.
```
options {
    ###
    forwarders {
        202.117.0.20;
        202.117.0.21;
        114.114.114.114;
    };
    ###
};
```
* Finally, we disable the dnssec option because all of the china's name server disbale the dnssec, so, our bind will query from root. It will slow down the query speed for the name such as `taobao.com`
```
options {
    ###
    dnssec-enable no;
    dnssec-validation no;
    ###
}
```

`db.interonet.org`
* BIND data file
* Edit this file accoring to the format.

> **NOTE:** add the Serial Number every time modify this file.

`db.10`
* BIND reverse data file
* Edit this file accoring to the format.

> **NOTE:** add the Serial Number every time modify this file.

## Reference
[Bind9ServerHowto](https://help.ubuntu.com/community/BIND9ServerHowto)
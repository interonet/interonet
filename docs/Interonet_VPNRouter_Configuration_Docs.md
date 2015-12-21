# 路由系统配置
## 接口配置
配置文件:/etc/network/interface
假设eth0已经配置好，PC可以通过eth0连接互联网。
配置eth1所示
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
## DNS配置
/etc/resolvconf/resolv.conf.d/base（这个文件默认是空的）
在里面插入：
```
nameserver 202.117.0.20
nameserver 202.117.0.21
```
如果有多个DNS就一行一个
修改好保存，然后执行
resolvconf -u
再看/etc/resolv.conf，最下面就多了2行：
```
nameserver 202.117.0.20
nameserver 202.117.0.21
```
## 转发功能
Linux系统自身就有IPv4包转发的功能，在/etc/sysctl.conf进行配置，打开IPv4转发功能:
```
net.ipv4.ip_forward=1
```
输入以下命令使得上面的设置生效(或者重启电脑)：
```
 sudo sysctl -p
 ```
 ## iptables SNAT配置
 ```
 sudo iptables -t nat -A POSTROUTING -s 10.0.0.0/8 -o eth0 -j MASQUERADE
 ```
 # PPTP的配置
 ## 安装pptp
 ```
 sudo apt-get install pptp
 ```
 ## 配置ip地址（编辑 /etc/pptp.conf）
 ```
localip 10.255.255.1
remoteip 10.255.255.2-10.255.255.254
```
## 配置dns（编辑 /etc/ppp/pptpd-options）
```
sudo vi /etc/ppp/pptpd-options
```
添加
```
ms-dns 202.117.0.20
ms-dns 202.117.0.21
```

## 配置用户名密码（编辑 /etc/ppp/chap-secrets）
```
# client        server  secret                  IP addresses
user1           pptpd   user1                   *
```
## 重启pptp服务
```
sudo /etc/init.d/pptpd restart
```

## 配置iptable转发 建立NAT转发可以实现pptp客户端外网的连接
```
sudo iptables -t nat -A POSTROUTING -s 10.255.255.0/24 -o eth0 -j MASQUERADE
```
> **注意下面为常见错误。该转发使得所有流量伪装为202.117.15.119，因此，无论怎么ping均可ping通。**

> 输入以下指令可以实现pptp客户端对内网的访问
> sudo iptables -t nat -A PREROUTING -d 10.0.0.0/8 -j DNAT --to 202.117.15.119(将内网IP伪装成外网IP)

在实现路由转发之后建立pptp连接会出现网页访问不了的情况，是因为限制了流量传输，解决此问题提升pptp VPN的速度可以输入以下2条指令
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
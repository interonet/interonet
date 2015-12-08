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
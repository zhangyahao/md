1.使用 hostname 修改当前主机名。
hostname new-hostname

2.修改 /etc/sysconfig/network  配置文件，以便下次重启的时，使用新的主机名。
打开 /etc/sysconfig/network 文件，修改 HOSTNAME=new-hostname.domainname。
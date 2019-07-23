1. 查找文件或文件夹  find / -name （文件名）
2.关闭防火墙 service iptables stop
3.永久关闭防火墙 chkconfig iptables off 需要重启
4.检查防火墙 chkconfig --list iptables
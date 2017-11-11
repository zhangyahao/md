#redis 配置
服务器中得防火墙需要关闭  运行redis服务时需要加载
redis得配置文件

Redis的目标定位，就是分布式的高速缓存，


第一步：从官网下载redis.xxx.tar.gz安装包，把安装包放入Linux-CentOS系统中
第二步：安装gcc/tcl
	yum -y install gcc
	yum -y install tcl
	查看组件状态：
 	yum list | grep gcc
 	yum list | grep tcl

第三步：安装redis程序
	tar xzf redis-4.0.2.tar.gz
	cd redis-4.0.2
	make

第四步：启动redis服务端程序
	cd /home/redis-4.0.2
	./src/redis-server redis.conf

在redis服务启动后，直接Ctrl+C即可关闭服务
默认未开启远程连接，编辑redis.conf，注释掉bind即可远程访问：#bind 127.0.0.1



关闭服务端：
./redis-cli -h 127.0.0.1 -p 7000 shutdown

关闭防火墙：
service iptables stop
chkconfig iptables off




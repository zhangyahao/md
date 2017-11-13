使用CentOS 安装jdk SE1.8
通过yum安装的 一般都是 OpenJDK
一般在windows平台上开发时都用的是SunJDK
为避免可能出现的问题  需要卸载自带的OpenJDK  安装SunJDK
先查看openJDK版本  
yum list installed |grep java
yum -y remove java-1.6.0-openjdk.x86_64    删除openjdk 1.6.0
想要安装SunJDK  要通过wget -c +url 来安装
wget支持http https  ftp协议
找到SunJDK的下载地址  

wget   http://download.oracle.com/otn-pub/java/jdk/8u111-b14/jdk-8u111-linux-x64.rpm 
如果不能下载  先在windows平台上 用Chrome下载  然后copy下载地址   通常url还带有一个超长的 请求参数
下载后直接安装    
rpm -ivh jdk-8u111-b14-jdk-8u111-linux-x64.rpm
安装完成后   无需配置环境变量

使用java -version
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
安装成功

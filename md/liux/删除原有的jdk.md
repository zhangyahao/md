一、卸载原有openjdk

rpm -qa | grep java
之后，将展示出来的全部卸载掉，我这里是5个

rpm -e --nodeps java-1.7.0-openjdk-1.7.0.111-2.6.7.2.el7_2.x86_64
rpm -e --nodeps java-1.7.0-openjdk-headless-1.7.0.111-2.6.7.2.el7_2.x86_64
rpm -e --nodeps python-javapackages-3.4.1-11.el7.noarch
rpm -e --nodeps javapackages-tools-3.4.1-11.el7.noarch
rpm -e --nodeps tzdata-java-2016h-1.el7.noarch
最后，再通过rpm -qa | grep java查看是否还有内容，若没有，说明卸载干净。

 

二、安装sun jdk

1、本地从Oracle官网下载jdk。

2、将本地下载的jdk传到虚拟机

scp jdk-8u102-linux-x64.tar.gz root@10.211.55.4:/opt/
3、解压

tar -xvf jdk-8u102-linux-x64.tar.gz
4、配置环境变量

vi /etc/profile
1 JAVA_HOME=/opt/jdk1.8.0_102
2 export JAVA_HOME
3 export PATH=$PATH:$JAVA_HOME/bin
source /etc/profile（是配置文件生效）
5、验证

java -version
1. 查看日志  日志位置
hadoop/logs/ hadoop-root-namenode-xujingxx.log 
2.没有datanode
先查看 Hadoop/etc/hadoop/  下的hadoop-site.xml 和hadoop-env.sh
配置是否错误  如果没有的话
  1.主机的话直接删除Hadoop/tmp 下的所有文件然后hadoop namenode -format
  2.从机直接删除hadoop/tmp 
 3.启动后不能访问web管理界面   如果启动项全部正常  先清一下浏览器缓存
 如果还是不行
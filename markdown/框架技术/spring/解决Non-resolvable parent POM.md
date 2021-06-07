在编译spring boot 多模块项目的时候，往往出现

Non-resolvable parent POM: Could not find artifact  后面跟一串其它信息，网上大部分解决方案是修改relativePath配置项，其实这是很不优雅的方式。


把父项目的modules里面的配置全部注释或者删除,然后编译并打包并安装，在提示成功后，再把原来注释活删除的配置项还原即可，然后再编译即可成功。
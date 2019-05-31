出现这样的原因应该是Maven插件的默认配置有问题。解决方法是在”pom.xml”里加入如下代码：
```aidl
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

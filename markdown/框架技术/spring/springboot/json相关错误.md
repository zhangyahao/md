```aidl
    org.springframework.web.util.NestedServletException: Handler processing failed; nested exception is java.lang.NoSuchMethodError: com.fasterxml.jackson.databind.JavaType.isReferenceType()Z
    省略200行
    ....
```
那么将jackson相关的包添加
```aidl
<!-- Spring 升级4+ 依赖的JSON包 -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.7.4</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.7.4</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.7.4</version>
</dependency>
<!-- /Spring 升级4+ 依赖的JSON包 -->
```
如果还是出现错误，请检查 Spring  的jar是不是4.5或者以下。或者把 jackson  包降低版本。基本能解决！
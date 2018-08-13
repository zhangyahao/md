1.  依赖maven
2.  在pom文件中添加maven插件依赖
    ```aidl
        <dependencies>
                <ependency>
                          <groupId>mysql</groupId>
                          <artifactId>mysql-connector-java</artifactId>
                          <version>5.1.39</version>
                      </dependency>
                      <dependency>
                          <groupId>org.mybatis</groupId>
                          <artifactId>mybatis</artifactId>
                          <version>3.4.5</version>
                      </dependency>
        </dependencies>
      <build>
            <plugins>
                ................
                <plugin>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-maven-plugin</artifactId>
                    <version>1.3.5</version>
                    <configuration>
                        <verbose>true</verbose>
                        <overwrite>true</overwrite>
                    </configuration>
                </plugin>
            </plugins>
        </build>

    ```
3.  添加配置文件 `generatorConfig.xml` 
4.  在maven中运行   mybatis-generator
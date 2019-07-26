1. 查找端口
   ```aidl
    netstat -ano |findstr 8080
    ```
2. 强制关闭tomcat
    ```aidl
       taskkill /f /pid ****
    ```
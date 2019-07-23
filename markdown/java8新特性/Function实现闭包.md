`Function<object，object>`闭包实现
1.  含义：表示该函数得入口参数为`object`，返回参数类型为`object`
2.  使用：一般配合`：：`使用
    ```aidl
           Function<String, String> func = String::toUpperCase;
           String s = func.apply("a");
           System.out.println(s);
    ```




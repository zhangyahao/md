1. 当string 直接引用变量进行拼接的时候，java先从内存中查找到变量地址，然后调用stringBuilder来进行  
    拼接，然后调用tostring方法，因此  
    ```aidl
         String s = "111";
         String a = s + "";
         System.out.println(s==a);
       输出  false
    ```
2.  当直接进行拼接时，java编译器可以直接进行优化，因此  
    ```aidl
          String s = "111";
          System.out.println(s == "111" + "");
          输出 true
    ```  
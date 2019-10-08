##对null 更优雅的操作

1.  of  
    ```aidl
     为非null的值创建一个Optional。  
     of方法通过工厂方法创建Optional类。需要注意的是，创建对象时传入的参数不能为null。  
     如果传入参数为null，则抛出NullPointerException 。
    ```
2. ofNullable  
    ```aidl
       为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。   
         List<String> s = new ArrayList<>();
         s.add("111");
         System.out.println(Optional.ofNullable(s)
    ```
3.  isPresent    
    ```aidl
        如果值存在返回true，否则返回false。
    ```
4.  get  
    ```aidl
     如果Optional有值则将其返回，否则抛出NoSuchElementException。
    ```     
5.  orElse  
    ```aidl
     如果有值则将其返回，否则返回指定的其它值。  
    
        
    ```
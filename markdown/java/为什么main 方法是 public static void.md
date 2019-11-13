1. main 方法是静态的（static）原因：  
    1.  正因为 main 方法是静态的，JVM 调用这个方法就不需要创建任何包含这个 main 方法的实例。 
    2.  因为 C 和 C++ 同样有类似的 main 方法作为程序执行的入口。
    3.  如果 main 方法不声明为静态的，JVM 就必须创建 main 类的实例，因为构造器可以被重载，JVM 就没法确定调用哪个 main 方法。
    4.  静态方法和静态数据加载到内存就可以直接调用而不需要像实例方法一样创建实例后才能调用，如果 main 方法是静态的，  
        那么它就会被加载到 JVM 上下文中成为可执行的方法。
2.  main 方法没有返回值（Void）原因：  
    ```aidl
        因为 main 返回任何值对程序都没任何意义，所以设计成 void，意味着 main 不会有任何值返回。
    ```   
3.  总结:   
    1.  main 方法必须声明为 public、static、void，否则 JVM 没法运行程序 。         
    2.  如果 JVM 找不到 main 方法就抛出 NoSuchMethodError:main 异常。
    3.  main 方式是程序的入口，程序执行的开始处。
    4.  main 方法被一个特定的线程 ”main” 运行，程序会一直运行直到 main 线程结束或者 non-daemon 线程终止。
    5.  当你看到“Exception in Thread main”,意味着异常来自于 main 线程。
    6.  你可以声明 main 方法使用 java1.5 的可变参数的方式如：public static void main(String... args)。
    7.  除了 static、void、和 public，你可以使用 final，synchronized、和 strictfp 修饰符在 main 方法的签名中，
        如：public strictfp final synchronized static void main(String[] args)。
    8.  main 方法在 Java 可以像其他方法一样被重载，但是 JVM 只会调用上面这种签名规范的 main 方法。     
    9.  可以使用 throws 子句在方法签名中，可以抛出任何 checked 和 unchecked 异常。
    10. 静态初始化块在 JVM 调用 main 方法前被执行，它们在类被 JVM 加载到内存的时候就被执行了。
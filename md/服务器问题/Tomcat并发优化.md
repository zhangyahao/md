###配置文件在 tomcate -->config --> server.xml

1.  调整连接器connector的并发处理能力
    在Tomcat 配置文件 server.xml 中的 <Connector ... /> 配置中
    1. 参数说明
        maxThreads  客户请求最大线程数
        
        minSpareThreads    Tomcat初始化时创建的 socket 线程数
        
        maxSpareThreads   Tomcat连接器的最大空闲 socket 线程数
        
        minProcessors：最小空闲连接线程数，用于提高系统处理性能，默认值为 10
        
        maxProcessors：最大连接线程数，即：并发处理的最大请求数，默认值为 75
        
        acceptCount：允许的最大连接数，应大于等于 maxProcessors ，默认值为 100
        
        enableLookups：是否反查域名，取值为： true 或 false 。为了提高处理能力，应设置为 false
        
        redirectPort        在需要基于安全通道的场合，把客户请求转发到基于SSL 的 redirectPort 端口
        
        acceptAccount       监听端口队列最大数，满了之后客户请求会被拒绝（不能小于maxSpareThreads  ）
        
        connectionTimeout：网络连接超时，单位：毫秒。设置为 0 表示永不超时，这样设置有隐患的。通常可设置为30000 毫秒。
        
        URIEncoding    URL统一编码
        
        其中和最大连接数相关的参数为maxProcessors 和 acceptCount 。如果要加大并发连接数，应同时加大这两个参数。
        
        web server允许的最大连接数还受制于操作系统的内核参数设置，通常 Windows 是 2000 个左右， Linux 是1000 个左右。
2.  缓存优化
    tomcat的maxThreads、acceptCount（最大线程数、最大排队数）
    maxThreads：tomcat起动的最大线程数，即同时处理的任务个数，默认值为200
    acceptCount：当tomcat起动的线程数达到最大时，接受排队的请求个数，默认值为100
    
    1. 这两个值作用：<br>
        1.  接受一个请求，此时tomcat起动的线程数没有到达maxThreads，tomcat会起动一个线程来处理此请求。
        2.  接受一个请求，此时tomcat起动的线程数已经到达maxThreads，tomcat会把此请求放入等待队列，等待空闲线程。
        3.  接受一个请求，此时tomcat起动的线程数已经到达maxThreads，等待队列中的请求个数也达到了acceptCount，此时tomcat
            会直接拒绝此次请求，返回connection refused
    2.  maxThreads如何配置  
        1.  如果我们的操作是纯粹的计算，那么系统响应时间的主要限制就是cpu的运算能力，此时maxThreads应该尽量设的小，降
                低同一时间内争抢cpu的线程个数，可以提高计算效率，提高系统的整体处理能力。  
        2.  如果我们的操作纯粹是IO或者数据库，那么响应时间的主要限制就变为等待外部资源，此时maxThreads应该尽量设的大，这
                样才能提高同时处理请求的个数，从而提高系统整体的处理能力。此情况下因为tomcat同时处理的请求量会比较大，
                所以需要关注一下tomcat的虚拟机内存设置和linux的open file限制。
        3.  出现的问题
    
                1.   maxThreads我设置的比较大比如3000，当服务的线程数大到一定程度时，一般是2000出头，单次请求的响应时间就会
                        急剧的增加。
                     原因可能是cpu在线程切换时消耗的时间随着线程数量的增加越来越大
                
    3. acceptCount的配置，我一般是设置的跟maxThreads一样大，这个值应该是主要根据应用的访问峰值与平均值来权衡配置的。
         
             如果设的较小，可以保证接受的请求较快相应，但是超出的请求可能就直接被拒绝
         
             如果设的较大，可能就会出现大量的请求超时的情况，因为我们系统的处理能力是一定的
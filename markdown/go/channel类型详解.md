1.  Channel是Go中的一个核心类型，你可以把它看成一个管道，通过它并发核心单元就可以发送或者接收数据进行通讯(communication)<br>
     经常用在select语句中，它的操作符是 `<-` (箭头的指向就是数据的流向)
    ```aidl
        ch <- v    // 发送值v到Channel ch中
        v := <-ch  // 从Channel ch中接收数据，并将数据赋值给v
    ```       
2.   channel必须先创建再使用:
      ```aidl
         ch := make(chan int)
      ```
3.  Channel类型的定义格式如下：
    ````aidl
        ChannelType = ( "chan" | "chan" "<-" | "<-" "chan" ) ElementType .
    ````
    它包括三种类型的定义。可选的<-代表channel的方向。如果没有指定方向，那么Channel就是双向的，既可以接收数据，也可以发送数据。
    ```aidl
        chan T          // 可以接收和发送类型为 T 的数据
        chan<- float64  // 只可以用来发送 float64 类型的数据
        <-chan int      // 只可以用来接收 int 类型的数据
    ```
    `<-`总是优先和最左边的类型结合。
       ```aidl
           chan<- chan int    // 等价 chan<- (chan int)
           chan<- <-chan int  // 等价 chan<- (<-chan int)
           <-chan <-chan int  // 等价 <-chan (<-chan int)
           chan (<-chan int)
       
       ```
4.  使用`make`初始化Channel,并且可以设置容量:
    容量(capacity)代表Channel容纳的最多的元素的数量，代表Channel的缓存的大小。
       ```aidl
         make(chan int, 100)
       ```
    如果没有设置容量，或者容量设置为0, 说明Channel没有缓存，只有sender和receiver都准备好了后它们的通讯(communication)才会发生(Blocking)。
    果设置了缓存，就有可能不发生阻塞， 只有buffer满了后 send才会阻塞， 而只有缓存空了后receive才会阻塞。一个nil channel不会通信。
    可以通过内建的`close`方法可以关闭Channel。
    
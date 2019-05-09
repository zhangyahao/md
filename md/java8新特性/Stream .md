1.  Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。
2.  数据源本身可以是无限的。
3.  对于基本数值型，目前有三种对应的包装类型 Stream： `IntStream、LongStream、DoubleStream`。当然也可以使用<br>
    `Stream<Integer>、Stream<Long> >、Stream<Double>`，但是数组类型消耗的资源较多。
4.  常见的构造流的方式:
    ```aidl
        // 1. Individual values
        Stream stream = Stream.of("a", "b", "c");
        // 2. Arrays
        String [] strArray = new String[] {"a", "b", "c"};
        stream = Stream.of(strArray);
        stream = Arrays.stream(strArray);
        // 3. Collections
        List<String> list = Arrays.asList(strArray);
    ```    
 5. [常用操作](https://github.com/zhangyahao/md/blob/master/src/main/java/util/StreamDemo.java)
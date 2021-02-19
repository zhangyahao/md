1.  Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。
2.  数据源本身可以是无限的。
3.  对于基本数值型，目前有三种对应的包装类型 Stream： `IntStream、LongStream、DoubleStream`。当然也可以使用<br>
    `Stream<Integer>、Stream<Long> >、Stream<Double>`，但是数组类型消耗的资源较多。
4.  常见的构造流的方式:
    基本类型创建方式：
       1.  of / builder： 可指定int流中包含的具体单个元素
       2.  range / rangeClosed ： 将指定范围内的元素都添加到int流中，前者不包含最后一个元素，后者包含
       3.  generate / iterate :  指定生成int流中int元素的生成函数，前者的生成函数没有入参，后者会将前一次调用结果作为下一次调用生成函数的入参
       4.  concat ：将两个int流合并成一个
       ```text
         @Test
            public void test() throws Exception {
                //包含指定的元素
        //        IntStream intStream=IntStream.of(1);
                //返回的int流中的元素是已经排序好的
                IntStream intStream=IntStream.of(1,3,2,5,4,6);
                print("of",intStream);
         
                //从11到16,不包含16
                intStream=IntStream.range(11,16);
                //从11到16,包含16
        //        intStream=IntStream.rangeClosed(11,16);
                print("range",intStream);
         
                //包含指定的元素,add方法底层也是调用accept方法，然后返回this
                //返回的int流中的元素顺序与添加顺序一致
                intStream=IntStream.builder().add(23).add(22).add(21).build();
                print("builder", intStream);
         
                //指定一个int生成函数
                //返回的int流中的元素不排序
                intStream=IntStream.generate(()->{
                    Random random=new Random();
                    return random.nextInt(100);
                }).limit(6);
                print("generate", intStream);
         
                //指定一个int生成函数，前一次执行函数的结果会作为下一次调用函数的入参
                //第一个参数seed就是第一次调用生成函数的入参
                //返回的int流中的元素不排序
                intStream=IntStream.iterate(1,x->{
                   int a=2*x;
                   if(a>16){
                       return a-20;
                   }else{
                       return a;
                   }
                }).limit(6);
                print("iterate", intStream);
            }
         
            @Test
            public void test2() throws Exception {
                IntStream streamA=IntStream.range(11,15);
                IntStream streamB=IntStream.range(6,10);
                //将两个IntStream 合并起来
                //返回的int流的元素顺序与添加的流的元素顺序一致，不排序
                IntStream streamC=IntStream.concat(streamA,streamB);
                print("concat", streamC);
            }
         
            private void print(String start, IntStream intStream){
                System.out.println("print for->"+start);
                intStream.forEach(x->{
                    System.out.println(x);
                });
            }
      ```
    普通流创建方式：
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
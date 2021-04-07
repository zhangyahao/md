1. `Function`包含四个方法：  
    1.   `R apply`  初始化载入参数
    2.   `compose`就近执行函数
    3.   `andThen`在之前函数执行完成后，执行后续函数。
    4.   `identity`返回当前执行的函数
          
2.  示例：
       ```text
        Function<Integer, Integer> times2 = i -> i * 2;
        Function<Integer, Integer> squared = i -> i * i;
        //8
        System.out.println(times2.apply(4));
        //16
        System.out.println(squared.apply(4));

        //32     先4×4然后16×2,先执行apply(4)，在times2的apply(16),先执行参数，再执行调用者。
        System.out.println(times2.compose(squared).apply(4));

        //64               先4×2,然后8×8,先执行times2的函数，在执行squared的函数。
        System.out.println(times2.andThen(squared).apply(4));

        //16
        System.out.println(Function.identity().compose(squared).apply(4));
       ```
3.  注：  
    1.  若使用此函数，必须使用 `apply` 初始化参数。
###lambda表达式是java8之后得新特性 非常得使用能够大大简化代码的长度
<br>
<br>
一. 对于匿名内部类的简化写法以多线程为例：
```$xslt
new Thread(()->System.out.Println("")).start();
```
如果需要各种逻辑分析
```$xslt
new Thread(()->{
// 逻辑代码写到这里
}).start();
```



二. 对于各种集合的遍历
以arraylist为例<br>
java8 前
```$xslt
for(i=0,i<list.size(),i++){
System.out.Println(list.get(i));
};
```


而用lambda表达式就可以很简单的获取<br>
```$xslt
list.forEach((n)->System.out.Println(n));
```

或者
```$xslt
list.forEach(System.out::println);
```

在java8中加入了stream 它将我们将要处理数据转变为流的方式处理然后对流进行处理<br>
可以直接创建stream对象比如：
```$xslt
Stream<String> str=Stream.of("sda","dad","d","dddd");
```
如果调用Stream.generator可以生成一个无限长的Stream。

那么我们对于集合或数组的处理就可以更加的简单 如下所示：
```$xslt
public static void main(args[]){
    List languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
 
    System.out.println("Languages which starts with J :");
    filter(languages, (str)->str.startsWith("J"));
 
    System.out.println("Languages which ends with a ");
    filter(languages, (str)->str.endsWith("a"));
 
    System.out.println("Print all languages :");
    filter(languages, (str)->true);
 
    System.out.println("Print no language : ");
    filter(languages, (str)->false);
 
    System.out.println("Print language whose length greater than 4:");
    filter(languages, (str)->str.length() > 4);
}
public static void filter(List names, Predicate condition) {
    for(String name: names)  {
        if(condition.test(name)) {
            System.out.println(name + " ");
        }
    }
}

```


如果我们用了stream 那么代码的简化程度将会更加的简洁<br>
Predicate类 Predicate.test  就相当于例子  当该类型的数值或者说样式 <br>
要求 和流中的比较 返回值是布尔类型
```$xslt
public class Test {
    private static List languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");

    public static void filter(List names, Predicate predicate) {
        names.stream().filter((name) -> (predicate.test(name))).forEach((name) -> {
            System.out.println(name + "");
        });
    }
    public static void main(String[] args) {
        //创建Predicate实例
        Predicate<String> str = (n) -> n.startsWith("J");
        filter(languages, str);
    }

}
```

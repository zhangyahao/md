demo
```java
package main.java.excel.dsada;

/**
 * @program: markdown
 * @description:
 * @author: Zhang
 * @create: 2020-11-16 15:03
 **/
public class BUildDemo {
    private String id;

    private String num;

    private BUildDemo(Builder builder) {
        id = builder.id;
        num = builder.num;
    }


    public static  class Builder{

        private String id;

        private String num;


//
//        public Builder(String id, String num) {
//            this.id = id;
//            this.num = num;
//        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder num(String num) {
            this.num = num;
            return this;
        }

        public BUildDemo Build() {
            return new BUildDemo(this);
        }


    }

    public static void main(String[] args) {
        BUildDemo dee = new Builder().id("3").num("33").Build();

    }

}

```
优点：
 1.  多参数
 2.  可以将实体类中部分必选项设置在一个构造器中。
 3.  增加可读性
 4.  线程安全
缺点：
 1. 代码过长
 2. 多个构造器增加了一部分开销



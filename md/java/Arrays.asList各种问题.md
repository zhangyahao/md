### 一、避免使用基本数据类型数组转换为列表
~~~
public class Test {
    public static void main(String[] args) {
        int[] a = {1,2,3,4};
        List list = Arrays.asList(a);
        System.out.println(list.size());  //1
    }

}
~~~
   使用基本数据类型转换时，有缺陷  必须使用包装类 要不然转换后的list中
   存入依然是list
  二、asList 产生的列表不可操作
  长度不可变
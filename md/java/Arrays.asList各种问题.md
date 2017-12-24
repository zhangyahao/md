### 一、避免使用基本数据类型数组转换为列表
将一个数组转化为一个List对象，这个方法会返回一个ArrayList类型的对象，
 这个ArrayList类并非java.util.ArrayList类，而是Arrays类的静态内部类！
 用这个对象对列表进行添加删除更新操作，就会报
 UnsupportedOperationException异常。
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
在计算机语言中任何语言对于float都是有精度损失的，具体原因[维基百科](https://zh.wikipedia.org/wiki/IEEE_754)解释。<br>
因此会出现
````aidl
        int i1 = 3;
		double d1 = 4.9;
		System.out.println(i1);
		System.out.println(d1);
		System.out.println(i1*d1);
输出：
    3
    4.9
    14.700000000000001

````
在java提供了BigDecimal作为精确运算。
两个工具类[提供较长的数据精度](https://github.com/zhangyahao/md/blob/master/src/main/java/util/ComputeUtil.java)  <br> [趋于正常的返回值](https://github.com/zhangyahao/md/blob/master/src/main/java/util/DecimalUtil.java)




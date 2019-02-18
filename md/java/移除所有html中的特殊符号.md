在apache commons-lang(2.3以上版本)中为我们提供了一个方便做转义的工具类，主要是为了防止sql注入，xss注入攻击的功能。总共提供了以下几个方法：
1. escapeSql 提供sql转移功能，防止sql注入攻击，例如典型的万能密码攻击' ' or 1=1 ' '
    ```aidl
       StringBuffer sql = new StringBuffer("select key_sn,remark,create_date from tb_selogon_key where 1=1 ");
       		if(!CommUtil.isEmpty(keyWord)){
       			sql.append(" and like '%" + StringEscapeUtils.escapeSql(keyWord) + "%'");
       		}
    ```   
2. escapeHtml /unescapeHtml  转义/反转义html脚本
    ```aidl
       System.out.println(StringEscapeUtils.escapeHtml("<a>dddd</a>"));   
       输出结果为：&lt;a&gt;dddd&lt;/a&gt;
    ```
    ```aidl
    System.out.println(StringEscapeUtils.unescapeHtml("&lt;a&gt;dddd&lt;/a&gt;"));   
    输出为：<a>ddd</a>
    ```
3. escapeJavascript/unescapeJavascript 转义/反转义js脚本
    ```aidl
    System.out.println(StringEscapeUtils.escapeJavaScript("<script>alert('1111')</script>"));   
    输出为：&lt;script&gt;alert('111')&lt;/script&gt;
    ```
4.  escapeJava/unescapeJava 把字符串转为unicode编码    
    ```aidl
        System.out.println(StringEscapeUtils.escapeJava("中国"));   
        输出为：用escapeJava方法转义之后的字符串为:/u4E2D/u56FD/u5171/u4EA7/u515A
    ```
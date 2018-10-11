1. dom4j中，获得Document对象的方式有三种
   1. 读取xml文件，获得Document对象
   ```$xslt
   SAXReader reader = new SAXReader();
   Document document = reader.read(new File("input.xml"));
   ```
   2. 解析xml形式的文本，得到Document对象，将字符串转换为
   ```$xslt
    String text = "<members></members>";
    Document document = DocumentHelper.parseText(text);
    ```
    3. 主动创建Document对象（直接创建）
    ```$xslt
    Document document = DocumentHelper.createDocument(); //创建根节点
    Element root = document.addElement("members");
    ```
2. 节点对象
    1. 获取文档的根节点：
    ```$xslt
    Element root = document.getRootElement();
    ```
    4. 取得某节点下所有名为“member“的子节点，并进行遍历
    ```$xslt
    List nodes = rootElm.elements("member");
    for (Iterator it = nodes.iterator(); it.hasNext();) {     
        Element elm = (Element) it.next();   
         // do something 
    }
    ```
    5. 对某节点下的所有子节点进行遍历（遍历方法2）
    ```$xslt
    for(Iterator it=root.elementIterator();it.hasNext();){       
        Element element = (Element) it.next();       
        // do something   
    }
    ```
    6. 在某节点下添加子节点
    ```$xslt
    Element ageElm = newMemberElm.addElement("age");
    ```
    7. 设置节点文本
    ```$xslt
    element.setText("29");
    ```
    8. 删除某节点
    ```$xslt
    //childElm是待删除的节点,parentElm是其父节点
    parentElm.remove(childElm);
    ```
    9. 添加一个CDATA节点
    CDATA部件<br>
    在CDATA内部的所有内容都会被解析器忽略。<br>
    如果文本包含了很多的"<"字符和"&"字符——就象程序代码一样，那么最好把他们都放到CDATA部件中。
    一个 CDATA 部件以"<![CDATA[" 标记开始，以"]]>"标记结束:
    **CDATA中不能再包含CDATA**
    ```$xslt
    Element contentElm =infoElm.addElement("content");
    contentElm.addCDATA(diary.getContent());
    ```
###net.sf.json办法
```$xslt
 JsonConfig jc = new JsonConfig();
 jc.setExcludes(new String[]{"属性"});
```
###jackson 办法
jackson可以直接在类上注解 直接在加载时忽略掉
```$xslt
@JsonIgnoreProperties({"忽略的属性"})
```
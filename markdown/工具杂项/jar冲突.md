1.  使用`maven`的`mvn dependency:tree`功能。但不一定能找到

2.  在冲突代码处，添加代码过滤。
    ```
        try{
          JSON.parseObject("...", Feature.OrderedField)
        }catch(Throwable e){
            String loc = "";
             String urlLoc = "";
             try {
               loc =
        
              <strong>Feature.class.getProtectionDomain().getCodeSource().getLocation().getFile();
              urlLoc =  URLDecoder.decode(loc, "UTF-8");</strong>
           } catch (Throwable e2) {
        
           }
        
           logger.info("** loc=" + LOCATION + "; URLLoc=" + URLLOCATION);
        }
    ```

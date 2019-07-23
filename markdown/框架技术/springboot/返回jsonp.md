1. 添加配置
```aidl
    package com.ccdt.amos.cirs.base.config;
    
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
    
    /**
     * @description: 返回jsonp
     * @author: Mr.zhang
     * @create: 2018-10-21 19:16
     **/
    
    @ControllerAdvice(basePackages = "controlle包路径")
    public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        public JsonpAdvice() {
            super("callback", "jsonp");
        }
    
    }

```


在使用 BigDecimal 进行科学运算的时候,`BigDecimal.valueOf(...)` 和 `new BigDecimal(...) `的使用效果不同。  
当 BigDecimal.valueOf(...) 的入参是 float 类型时，BigDecimal 会把入参强制转换成 double 类型。  
使用 new BigDecimal(String val) 来获得浮点数对应的 BigDecimal 对象，进而完成浮点数精确运算。    

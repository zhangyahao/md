### equals

1.  特点
    1.  自反性:
         对于任何非null的引用值 x`x.equals(x)` 必选返回 `true`
         
    2.  对称性：
        对于任何非空的引用值 x，y `x.equals(y)`则 `y.equals(x)`        
        
    3.  传递性：
        对于任何非空的引用值 x,y,z 如果 `x.equals(y)`为true，并且 `x.equls(z)`为true。则`y.equals(z)`一定为true    
        
    4.  一致性：    
        对于任何非空的引用值x,y  如果 `x.equals(y)` 只要比较操作的对象没被改变，那么不论多少次调用  `x.equals(y)`
        结果始终不变
        
    5.  非空性：
        对于任何非null的引用， `x.equals(null)`必须返回false。    

2.  注意事项
    1.  object含有equals方法，任何继承了他的方法都可以覆盖 
       覆盖equals需要注意：
                    1.类的每个实例本质上都是唯一的.
                    2.不关系类是否提供了逻辑相等的测试功能. random类中 两个随机数是否相等，那么从object中继承的
                       equals方法就足够了。
                    3. 超类覆盖了equals方法，那么子类继承过来的同样适用于子类。
                    4. 类是私有的或者包级私有的，同时确定他的equals方法永远不会被调用，防止意外调用直接覆盖掉
                        ``
                        public boolean equals(object o){
                        throws new  assertionError();
                        }
                        `` 
       如果类有自己的特有的逻辑相等的情况下，且超类没有覆盖equals的情况下，可以覆盖equals。
       对于单例模式的类来讲不需要覆盖equals方法。对于单例来讲，逻辑相等和对象相等是相同的。                    
       
    2. 我们无法在扩展可实例化的类的同时，既增加组件，同时保留equals约定.反之，对于抽象类则可以。  
        只要是不可能直接常见超类的实例，就可以。
        
    3.  无论类是不是可变的，都不要使类依赖于不可靠的资源。比如`java.net.URL`的equals。
    
    4.  测试非空：
         一般来讲我们会这样测试：
            
                 public boolean equals (object o){
                    if(o==null){
                    return false:
                     }
                 }
            
          但这项测试是不必要的，equals方法必须使参数转换成适当的类型，在此之前会先调用 `instanceof`操作符，检查参数的类型
          因此，可改为  
          
                public boolean equals (object o){
                                if(o.instanceof MyType){
                                return false:
                                 }
                             }
          
3.  实现equals方法的诀窍：
    1. 使用 `==` 操作符检查对象的引用
    2. 使用 `instanceof` 检查参数是否是正确的类型。
    3. 把参数转换为正确的类型
    4. 检查类的每个关键域，检查参数中的域是否与该对象对应的域匹配。
    5. 覆盖equals时，`equals ` 方法的形参必须是 **_object_**     
    
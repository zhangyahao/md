#spring注解
1. 声明bean注解
    1. @Component 泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
    2. @service   用于service层
    3. @Controller 用于标注控制层组件（如struts中的action）
    4. @Repository  用于dao层
2. 注入Bean注解
    1. @Autowired  直接注入
    2. @Resource  默认按名称装配，当找不到与名称匹配的bean才会按类型装配。
    
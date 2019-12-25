###1. 应用在方法上  
 被`@ModelAttribute`注解了的方法会在每个 `controller`前被调用，因此url过多时谨慎使用
   1.  使用`@ModelAttribute`注解无返回值的方法
        ```java
        @Controller
        @RequestMapping(value = "/modelattribute")
        public class ModelAttributeController {
        
            @ModelAttribute
            public void myModel(@RequestParam(required = false) String abc, Model model) {
                model.addAttribute("attributeName", abc);
            }
        
            @RequestMapping(value = "/method")
            public String method() {
                return "method";
            }
        }

       ```
       相当于
       ```java
            @RequestMapping(value = "/method")
            public String method(@RequestParam(required = false) String abc, Model model) {
                model.addAttribute("attributeName", abc);
                return "method";
            } 
        ```
   2.  使用`@ModelAttribute`注解带有返回值的方法     
        ```java
            @ModelAttribute
            public String myModel(@RequestParam(required = false) String abc) {
                return abc;
            }
            
            @ModelAttribute
            public Student myModel(@RequestParam(required = false) String abc) {
                Student student = new Student(abc);
                return student;
            }
            
            @ModelAttribute
            public int myModel(@RequestParam(required = false) int number) {
                return number;
            } 
       ```
       相当于
        ```java
           model.addAttribute("string", abc);
           model.addAttribute("int", number);
           model.addAttribute("student", student);
        ```
       这样写出来后默认的key局限性太大，而且很难接受key 为 `string`,`int`。因此可以在` @ModelAttribute`添加属性`@ModelAttribute(value = "num")`
       这样就相当于` model.addAttribute("num", adc)`
###2.  使用@ModelAttribute注解的参数
   使用`@ModelAttribute`注解的参数，意思是从前面的Model中提取对应名称的属性。
   ```java
     @RequestMapping(value = "/param")
        public String param(@ModelAttribute("attributeName") String str,
                           @ModelAttribute("name") String str2,
                           @ModelAttribute("age") int str3) {
            return "param";
        }
```
###3.  应用在方法上，并且方法上也使用了@RequestMapping
  不论返回什么，都是放入到`Model`中的值。
  ````java
@Controller
@RequestMapping(value = "/modelattribute")
public class ModelAttributeController {

    @RequestMapping(value = "/test")
    @ModelAttribute("attributeName")
    public String test(@RequestParam(required = false) String name) {
        return name;
    }
}

 ````
Model属性名称由 `@ModelAttribute(value=””)`指定，相当于在request中封装了key=attributeName，value=name

                                        
                                         
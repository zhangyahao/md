1.   go的继承不是严格意义上的继承，go不支持继承，但是可以实现继承
2.   go能被称为有继承的原因是因为它有匿名嵌套功能：
        ```aidl
         type Pet struct {
         
         }
         func(p *Pet) Speak() {
         	fmt.Println("。。。")
         }
         func (p *Pet)SpeakTo(name string)  {
         	p.Speak()
         	fmt.Printf(" ",name)	
         }
         type Dog struct {
         	Pet
         }
        ```
      当这样嵌套实现后，Dog就可以直接调用Pet的方法，因此很多人都认为这就是go的继承。<br>
      如果这是继承，那么当我们“重载”了父类的方法后
      ```aidl
         func (d *Dog)Speak()  {
         	fmt.Printf("Wang!")
         }
      ```
       理论上来讲，当子类调用`SpeakTo`时，应当优先调用子类重载后的方法`Speak`，可以如下声明
       ```aidl
        var dog  Pet =new(Dog)
       ```
       因为go是不支持类型转换的，因此是编译都不能通过，因此go首先不支持的就是继承的[lsp](https://baike.baidu.com/item/LSP/3156621?fr=aladdin)原则。<br>
       如果不适用lsp原则，直接调用
       ```aidl
         dog:=new(Dog)
         dog.Speak()
         dog.SpeakTo("123")

       ```
       输出的是
       ```aidl
         Wang!
         。。。
         123
       ``` 
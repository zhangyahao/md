1. go 表达式go f(x, y, z)会启动一个新的goroutine运行函数f(x, y, z)。函数f，变量x、y、z的值是在原goroutine计算的，
    只有函数f的执行是在新的goroutine中的。显然，新的goroutine不能和当前go线程用同一个栈，否则会相互覆盖。所以对go关键字的调
    用协议与普通函数调用是不同的。
    然后，将goroutine添加进去，然后调用。示例
    ````aidl
    func main() {
       //创建逻辑处理器 
    	runtime.GOMAXPROCS(1)
 	    //goroutine计数器
    	var wg sync.WaitGroup
 	    //设置计算器
    	wg.Add(2)
    	go func(){
 	         //添加计数执行完毕 计数器-1
    		defer wg.Done()
    		for i:=1;i<100;i++ {
    			fmt.Println("A:",i)
    		}
    	}()
    	go func(){
    		defer wg.Done()
    		for i:=1;i<100;i++ {
    			fmt.Println("B:",i)
    		}
    	}()
 	    //当计数器大于0阻塞，为0时，也就是线程都运行结束后运行
    	wg.Wait()
    }

    ````
    [详解](https://www.flysnow.org/2017/04/11/go-in-action-go-goroutine.html)
    ```aidl
       runtime.GOMAXPROCS(1)
    ```
    创建调度器，不创建调度器上边得代码依然可以用，与java相同，两个输出会交叉打印。默认情况下，Go默认是给每个可用的物理处理器
    都分配一个逻辑处理器，当不强制要求时，会根据机器cpu性能默认创建多个调度器，并不是越多得调度器就更好。也可以
    ```aidl
       runtime.GOMAXPROCS(runtime.NumCPU(1))
    ```
    
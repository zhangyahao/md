1. 因为go天生的支持多线程，以及channel和goroutine的存在，当多个函数配合进行操作时，<br>
   可以自由的将一个每个方法定义需要的goroutine，更合理的分配服务器资源
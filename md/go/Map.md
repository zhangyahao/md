1.  ###定义 Map
    ```aidl
        /* 声明变量，默认 map 是 nil */
        var map_variable map[key_data_type]value_data_type
        
        /* 使用 make 函数 */
        map_variable := make(map[key_data_type]value_data_type)
    ```
    如果不初始化 map，那么就会创建一个 nil map。nil map 不能用来存放键值对
2.  ###map相关函数   
    1. 查看map中有无该key，s为string，ok为布尔。可随意定义 
        ```aidl
            var stringMap map[string]string
            stringMap = make(map[string]string)
            	s, ok :=stringMap["3"]
            	if ok {
            		fmt.Println("存在",s)
            	}else {
            		fmt.Println("不存在")
            	}
        	
        ```
    2.  delete() 函数    <br>
        delete() 函数用于删除集合的元素, 参数为 map 和其对应的 key。
        ```aidl
            delete(stringMap, "3")
        ```
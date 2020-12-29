1. ` @Document`  
    在项目启动后会减产是否含有此注解标识的`index`，若没有就创建。有会出错，因此用在第一次创建`index`时。
    ````text
       @Document(indexName = "xxx", type = "xxx", shards = 1, replicas = 1)
    ````
2.   `@Id `    指定某项为id
3.   `@Field` 为`mapping`中，每条文档的属性。含有所有的属性。 
      ```text
          @Field(type = FieldType.Text, analyzer = "")
      ```
      若为时间，需要搭配`@JsonFormat`来使用
      ```text
            @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyyMMdd HH:mm:ss")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
      ```